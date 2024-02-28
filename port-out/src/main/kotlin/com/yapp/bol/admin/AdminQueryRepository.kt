package com.yapp.bol.admin

import com.yapp.bol.auth.UserId

interface AdminQueryRepository {
    fun getAdmin(userId: UserId): Admin
}
