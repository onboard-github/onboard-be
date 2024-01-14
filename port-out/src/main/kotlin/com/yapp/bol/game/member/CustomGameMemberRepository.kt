package com.yapp.bol.game.member

import com.yapp.bol.group.member.MemberId

interface CustomGameMemberRepository {
    fun getMatchCount(memberId: MemberId): Long
}
