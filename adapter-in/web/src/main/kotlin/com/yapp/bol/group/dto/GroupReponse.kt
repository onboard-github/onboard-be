package com.yapp.bol.group.dto

import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.MemberId

data class GroupResponse(
    val id: GroupId,
    val name: String,
    val description: String,
    val organization: String?,
    val profileImageUrl: String,
    val memberId: MemberId
)

fun GroupWithMemberId.toResponse(): GroupResponse =
    GroupResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        organization = this.organization,
        profileImageUrl = this.profileImage.getUrl(),
        memberId = this.memberId
    )
