package com.yapp.bol.admin

import com.yapp.bol.auth.UserId

data class Admin(
    val userId: UserId,
    val allowRoles: Set<AdminRole>,
    val requestRoles: Set<AdminRole>,
)
