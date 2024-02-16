package com.yapp.bol.admin

import com.yapp.bol.auth.UserId

interface AdminQueryRepository {
    fun getRoles(userId: UserId): List<AdminRole>
}
