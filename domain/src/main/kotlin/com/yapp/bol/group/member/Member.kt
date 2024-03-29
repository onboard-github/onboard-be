package com.yapp.bol.group.member

import com.yapp.bol.InvalidMemberRoleException
import com.yapp.bol.InvalidNicknameException
import com.yapp.bol.auth.UserId
import com.yapp.bol.validate.NicknameValidator

@JvmInline
value class MemberId(val value: Long) {
    override fun toString(): String = value.toString()
}

abstract class Member internal constructor(
    val id: MemberId,
    val userId: UserId?,
    val nickname: String,
    val level: Int,
) {
    val role: MemberRole = when {
        isOwner() -> MemberRole.OWNER
        isGuest() -> MemberRole.GUEST
        isHost() -> MemberRole.HOST
        else -> throw InvalidMemberRoleException
    }

    init {
        if (NicknameValidator.validate(nickname).not()) throw InvalidNicknameException(nickname)

        if (userId == null && isGuest().not()) throw InvalidMemberRoleException
    }

    fun isOwner(): Boolean = this is OwnerMember
    fun isGuest(): Boolean = userId == null || this is GuestMember
    fun isHost(): Boolean = this is HostMember

    fun changeNickname(nickname: String): Member {
        when {
            isOwner() -> return OwnerMember(
                id = id,
                userId = this.userId!!,
                nickname = nickname,
                level = level,
            )
            isHost() -> return HostMember(
                id = id,
                userId = this.userId!!,
                nickname = nickname,
                level = level,
            )
            else -> throw InvalidMemberRoleException
        }
    }

    companion object {
        const val MAX_NICKNAME_LENGTH = 10
        const val MIN_NICKNAME_LENGTH = 1
    }
}
