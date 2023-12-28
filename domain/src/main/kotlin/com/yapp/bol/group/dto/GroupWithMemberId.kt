package com.yapp.bol.group.dto

import com.yapp.bol.group.Group
import com.yapp.bol.group.GroupBasicInfo
import com.yapp.bol.group.member.MemberId
import com.yapp.bol.group.member.MemberList

data class GroupWithMemberId(
    val group: GroupBasicInfo,
    val memberId: MemberId
) : GroupBasicInfo by group {
    companion object {
        fun of(group: Group, memberId: MemberId) = GroupWithMemberId(
            group = group,
            memberId = memberId
        )
    }
}
