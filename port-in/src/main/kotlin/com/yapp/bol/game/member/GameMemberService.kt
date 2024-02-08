package com.yapp.bol.game.member

import com.yapp.bol.game.GameId
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.MemberId

interface GameMemberService {
    fun getMatchCountByMemberId(memberId: MemberId): Long
    fun getOrCreateGameMember(groupId: GroupId, gameId: GameId, memberId: MemberId): GameMember
}
