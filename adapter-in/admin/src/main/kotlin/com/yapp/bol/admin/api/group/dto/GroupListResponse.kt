package com.yapp.bol.admin.api.group.dto

import com.yapp.bol.group.GroupId
import com.yapp.bol.group.dto.GroupWithMemberCount

data class GroupListResponse(
    val list: List<GroupListItemResponse>,
    val totalCount: Long,
)

data class GroupListItemResponse(
    val id: GroupId,
    val name: String,
    val description: String,
    val organization: String?,
    val accessCode: String,
    val memberCount: Int,
)

fun GroupWithMemberCount.toResponse(): GroupListItemResponse = GroupListItemResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    organization = this.organization,
    accessCode = this.accessCode,
    memberCount = this.memberCount,
)
