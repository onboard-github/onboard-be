package com.yapp.bol.group.member.nickname

data class NicknameValidation(
    val isAvailable: Boolean,
    val reason: NicknameValidationReason?,
) {
    companion object {
        fun available(): NicknameValidation = NicknameValidation(
            isAvailable = true,
            reason = null,
        )

        fun duplicated(): NicknameValidation = NicknameValidation(
            isAvailable = false,
            reason = NicknameValidationReason.DUPLICATED_NICKNAME,
        )

        fun invalid(): NicknameValidation = NicknameValidation(
            isAvailable = false,
            reason = NicknameValidationReason.INVALID_NICKNAME,
        )
    }
}
