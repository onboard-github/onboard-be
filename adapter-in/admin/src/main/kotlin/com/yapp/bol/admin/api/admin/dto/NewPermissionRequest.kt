package com.yapp.bol.admin.api.admin.dto

import com.yapp.bol.admin.AdminRole

data class NewPermissionRequest(
    val role: List<AdminRole>,
    val memo: String,
)
