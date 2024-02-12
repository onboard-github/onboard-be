package com.yapp.bol.api.group.dto

import com.yapp.bol.group.GroupId
import com.yapp.bol.group.dto.GroupWithMemberCount

data class GroupListResponse(
    val list: List<GroupListItemResponse>
)

data class GroupListItemResponse(
    val id: GroupId,
    val name: String,
    val organization: String?,
    val accessCode: String,
    val memberCount: Int,
)

fun GroupWithMemberCount.toResponse(): GroupListItemResponse = GroupListItemResponse(
    id = this.id,
    name = this.name,
    organization = this.organization,
    accessCode = this.accessCode,
    memberCount = this.memberCount,
)
