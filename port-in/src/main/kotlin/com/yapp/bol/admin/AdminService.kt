package com.yapp.bol.admin

import com.yapp.bol.auth.UserId

interface AdminService {
    fun getRoleList(userId: UserId): List<AdminRole>
}
