package com.yapp.bol.group.member

import com.yapp.bol.NotFoundMemberException
import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
internal class MemberCommandRepositoryImpl(
    private val memberRepository: MemberRepository,
) : MemberCommandRepository {
    override fun createMember(groupId: GroupId, member: Member): Member {
        return memberRepository.save(member.toEntity(groupId.value)).toDomain()
    }

    @Transactional
    override fun updateGuestToHost(groupId: GroupId, memberId: MemberId, userId: UserId) {
        val member = memberRepository.findByIdOrNull(memberId.value) ?: throw NotFoundMemberException
        if (member.userId != null || member.groupId != groupId.value) throw NotFoundMemberException

        member.toHost(userId.value)
        memberRepository.save(member)
    }

    @Transactional
    override fun updateMemberInfo(groupId: GroupId, memberId: MemberId, nickname: String): Member {
        val member = memberRepository.findByIdOrNull(memberId.value)?.toDomain()
            ?: throw NotFoundMemberException

        return memberRepository.save(member.changeNickname(nickname).toEntity(groupId.value)).toDomain()
    }

    @Transactional
    override fun assignOwner(groupId: GroupId, originOwnerId: MemberId, targetMemberId: MemberId) {
        val originOwner = memberRepository.findByIdOrNull(originOwnerId.value) ?: throw NotFoundMemberException
        val targetMember = memberRepository.findByIdOrNull(targetMemberId.value) ?: throw NotFoundMemberException

        originOwner.toHost()
        targetMember.toOwner()

        memberRepository.saveAll(listOf(originOwner, targetMember))
    }

    override fun deleteMember(memberId: MemberId) {
        memberRepository.deleteById(memberId.value)
    }

    @Transactional
    override fun deleteAllMember(groupId: GroupId) {
        memberRepository.deleteAllByGroupId(groupId.value)
    }
}
