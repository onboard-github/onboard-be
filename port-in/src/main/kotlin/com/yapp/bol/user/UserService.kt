package com.yapp.bol.user

import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId

interface UserService {
    fun getUser(userId: UserId): User?

    fun putUser(user: User)

    fun getUserMatchCount(userId: UserId): Long

    fun getMemberMatchCount(groupId: GroupId, userId: UserId): Long
    
    fun deleteUser(userId: UserId)
}
