package com.yapp.bol.admin

import com.yapp.bol.auth.UserId

interface AdminService {
    fun getAvailableRoleList(userId: UserId): Set<AdminRole>

    fun requestRole(userId: UserId, role: List<AdminRole>, memo: String)
}
