package com.yapp.bol.group.dto

import com.yapp.bol.group.GroupId

data class JoinedGroupDto(
    val groupId: GroupId,
    val groupName: String,
    val nickname: String,
    val organization: String?,
    val matchCount: Long
)
