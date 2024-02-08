package com.yapp.bol.auth

@JvmInline
value class UserId(val value: Long) {
    override fun toString(): String = value.toString()
}

data class AuthUser(
    val id: UserId,
)
