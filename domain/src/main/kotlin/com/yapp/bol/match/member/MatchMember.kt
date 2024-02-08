package com.yapp.bol.match.member

import com.yapp.bol.group.member.MemberId

@JvmInline
value class MatchMemberId(val value: Long) {
    override fun toString(): String = value.toString()
}

data class MatchMember(
    val id: MatchMemberId,
    val memberId: MemberId,
    val score: Int,
    val ranking: Int,
)
