package com.yapp.bol.group.member

import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId

interface MemberCommandRepository {
    fun createMember(groupId: GroupId, member: Member): Member
    fun updateGuestToHost(groupId: GroupId, memberId: MemberId, userId: UserId)
    fun updateMemberInfo(groupId: GroupId, memberId: MemberId, nickname: String): Member
    fun deleteMember(memberId: MemberId)

    fun deleteAllMember(groupId: GroupId)
    fun assignOwner(groupId: GroupId, originOwnerId: MemberId, targetMemberId: MemberId)
}
