package com.yapp.bol.admin

enum class AdminRoleState(
    val descriptoin: String,
) {
    REQUEST("Role 요청"),
    ALLOW("Role 사용 가능"),
    DENY("Role 요청 거절"),
}
