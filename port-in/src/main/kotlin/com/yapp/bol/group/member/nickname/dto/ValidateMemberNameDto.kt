package com.yapp.bol.group.member.nickname.dto

import com.yapp.bol.group.member.nickname.NicknameValidationReason

data class ValidateMemberNameDto(
    val isAvailable: Boolean,
    val reason: NicknameValidationReason?
)
