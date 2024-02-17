package com.yapp.bol.admin

import com.yapp.bol.auth.UserId

fun <T> checkPermission(userId: UserId, permission: AdminPermission, run: () -> T) : T{
    return run()
}
