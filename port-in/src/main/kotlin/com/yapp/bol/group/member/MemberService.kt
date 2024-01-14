package com.yapp.bol.group.member

import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.dto.PaginationCursorMemberRequest
import com.yapp.bol.group.member.nickname.NicknameValidation
import com.yapp.bol.pagination.cursor.SimplePaginationCursorResponse

interface MemberService {
    fun validateMemberNickname(groupId: GroupId, nickname: String): NicknameValidation

    fun createHostMember(userId: UserId, groupId: GroupId, nickname: String): HostMember

    fun createGuestMember(groupId: GroupId, nickname: String): GuestMember

    fun getMembers(request: PaginationCursorMemberRequest): SimplePaginationCursorResponse<Member, String>

    fun findMembersByGroupId(groupId: GroupId): List<Member>

    fun updateMemberInfo(groupId: GroupId, memberId: MemberId, nickname: String): Member

    fun findMemberByGroupIdAndUserId(groupId: GroupId, userId: UserId): Member?

    fun deleteMyMember(groupId: GroupId, userId: UserId)

    fun findByUserId(userId: UserId): List<Member>

    fun assignOwner(
        groupId: GroupId,
        originOwnerId: UserId,
        targetMemberId: MemberId,
    )
}
