package com.yapp.bol.group.dto

import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.MemberId

data class JoinedGroupDto(
    val groupId: GroupId,
    val groupName: String,
    val nickname: String,
    val organization: String?,
    val matchCount: Long,
    val memberId: MemberId
)
