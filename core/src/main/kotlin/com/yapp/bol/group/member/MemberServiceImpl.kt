package com.yapp.bol.group.member

import com.yapp.bol.CannotDeleteOnlyOneMemberException
import com.yapp.bol.CannotDeleteOwnerException
import com.yapp.bol.DuplicatedMemberNicknameException
import com.yapp.bol.ForbiddenMemberException
import com.yapp.bol.InvalidMemberRoleException
import com.yapp.bol.InvalidRequestException
import com.yapp.bol.NotFoundMemberException
import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.dto.PaginationCursorMemberRequest
import com.yapp.bol.group.member.nickname.NicknameValidation
import com.yapp.bol.pagination.cursor.SimplePaginationCursorResponse
import com.yapp.bol.validate.NicknameValidator
import org.springframework.stereotype.Service

@Service
internal class MemberServiceImpl(
    private val memberQueryRepository: MemberQueryRepository,
    private val memberCommandRepository: MemberCommandRepository,
) : MemberService {
    override fun validateMemberNickname(groupId: GroupId, nickname: String): NicknameValidation =
        when {
            validateUniqueNickname(groupId, nickname).not() -> NicknameValidation.duplicated()
            NicknameValidator.validate(nickname).not() -> NicknameValidation.invalid()
            else -> NicknameValidation.available()
        }

    override fun createHostMember(userId: UserId, groupId: GroupId, nickname: String): HostMember {
        if (validateUniqueNickname(groupId, nickname).not()) throw DuplicatedMemberNicknameException

        val member = HostMember(
            userId = userId,
            nickname = nickname,
        )

        return memberCommandRepository.createMember(groupId, member) as HostMember
    }

    override fun createGuestMember(groupId: GroupId, nickname: String): GuestMember {
        val member = GuestMember(nickname = nickname)

        return memberCommandRepository.createMember(groupId, member) as GuestMember
    }

    override fun getMembers(request: PaginationCursorMemberRequest): SimplePaginationCursorResponse<Member, String> =
        memberQueryRepository.getMemberListByCursor(request)

    override fun findMembersByGroupId(groupId: GroupId): List<Member> =
        memberQueryRepository.findByGroupId(groupId)

    override fun updateMemberInfo(
        groupId: GroupId,
        memberId: MemberId,
        nickname: String,
    ): Member {
        if (validateUniqueNickname(groupId, nickname).not()) throw DuplicatedMemberNicknameException

        return memberCommandRepository.updateMemberInfo(groupId, memberId, nickname)
    }

    private fun validateUniqueNickname(groupId: GroupId, nickname: String): Boolean =
        memberQueryRepository.findByNicknameAndGroupId(nickname, groupId) == null

    override fun deleteMyMember(groupId: GroupId, userId: UserId) {
        val member = findMemberByGroupIdAndUserId(groupId, userId)
            ?: throw NotFoundMemberException

        if (memberQueryRepository.getCountExceptionGuest(groupId) == 1) throw CannotDeleteOnlyOneMemberException
        if (member.isOwner()) throw CannotDeleteOwnerException

        memberCommandRepository.deleteMember(member.id)
    }

    override fun findMemberByGroupIdAndUserId(groupId: GroupId, userId: UserId): Member? {
        return memberQueryRepository.findByGroupIdAndUserId(groupId, userId)
    }

    override fun assignOwner(
        groupId: GroupId,
        originOwnerId: UserId,
        targetMemberId: MemberId,
    ) {
        val originOwner = findMemberByGroupIdAndUserId(groupId, originOwnerId)
            ?: throw NotFoundMemberException

        if (originOwner.isOwner().not()) {
            throw ForbiddenMemberException
        }

        val targetMember = memberQueryRepository.findByIdAndGroupId(
            memberId = targetMemberId,
            groupId = groupId,
        ) ?: throw NotFoundMemberException

        if (targetMember.isGuest()) {
            throw InvalidMemberRoleException
        }

        if (originOwner.id == targetMember.id) {
            throw InvalidRequestException("자기 자신을 그룹장으로 임명할 수 없습니다.")
        }

        memberCommandRepository.assignOwner(
            groupId = groupId,
            originOwnerId = originOwner.id,
            targetMemberId = targetMember.id,
        )
    }

    override fun findByUserId(userId: UserId): List<Member> {
        return memberQueryRepository.findByUserId(userId)
    }
}
