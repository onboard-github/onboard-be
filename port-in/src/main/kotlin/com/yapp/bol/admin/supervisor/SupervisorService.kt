package com.yapp.bol.admin.supervisor

import com.yapp.bol.auth.UserId

interface SupervisorService {
    fun getRoleList(userId: UserId): List<AdminRole>
}
