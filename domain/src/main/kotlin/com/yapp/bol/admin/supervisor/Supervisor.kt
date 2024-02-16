package com.yapp.bol.admin.supervisor

import com.yapp.bol.auth.UserId

data class Supervisor(
    val userId: UserId,
    val name: String,
    val roles: Set<AdminRole>,
)
