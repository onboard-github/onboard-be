package com.yapp.bol.group.dto

import com.yapp.bol.auth.UserId

data class CreateGroupRequest(
    val name: String,
    val description: String,
    val organization: String?,
    val profileImageUrl: String?,
    val profileImageUuid: String?,
    val nickname: String?,
)

fun CreateGroupRequest.toDto(ownerId: UserId) = CreateGroupDto(
    name = name,
    description = description,
    organization = organization,
    profileImageUrl = profileImageUrl,
    profileImageUuid = profileImageUuid,
    ownerId = ownerId,
    nickname = nickname,
)
