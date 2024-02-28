package com.yapp.bol.admin

import com.yapp.bol.auth.UserId

interface AdminCommandRepository {
    fun requestRole(userId: UserId, roleList: List<AdminRole>, memo: String)
}
