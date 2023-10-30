package com.yapp.bol.group.member.dto

import com.yapp.bol.group.member.nickname.NicknameValidation
import com.yapp.bol.group.member.nickname.NicknameValidationReason

data class NicknameValidationResponse(
    val isAvailable: Boolean,
    val reason: NicknameValidationReason?,
)

fun NicknameValidation.toResponse(): NicknameValidationResponse = NicknameValidationResponse(
    isAvailable = this.isAvailable,
    reason = this.reason,
)
