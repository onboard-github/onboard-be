package com.yapp.bol.group.member

import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.dto.PaginationCursorMemberRequest
import com.yapp.bol.pagination.cursor.SimplePaginationCursorResponse

interface MemberQueryRepository {
    fun findByNicknameAndGroupId(nickname: String, groupId: GroupId): Member?
    fun findByGroupId(groupId: GroupId): List<Member>
    fun findByGroupIdAndUserId(groupId: GroupId, userId: UserId): Member?
    fun findByIdAndGroupId(memberId: MemberId, groupId: GroupId): Member?
    fun getMemberListByCursor(request: PaginationCursorMemberRequest): SimplePaginationCursorResponse<Member, String>
    fun findOwner(groupId: GroupId): OwnerMember
    fun getCount(groupId: GroupId): Int
    fun findByUserId(userId: UserId): List<Member>
    fun findMembersIdsByUserId(userId: UserId): List<MemberId>
    fun getCountExceptionGuest(groupId: GroupId): Int
}
