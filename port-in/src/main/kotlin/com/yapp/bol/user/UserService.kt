package com.yapp.bol.user

import com.yapp.bol.auth.UserId

interface UserService {
    fun getUser(userId: UserId): User?

    fun putUser(user: User)

    fun getMatchCountByUserId(userId: UserId): Long

    fun deleteUser(userId: UserId)
}
