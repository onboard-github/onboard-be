package com.yapp.bol.game.rating.dto

import com.yapp.bol.game.member.GameMember
import kotlin.math.max

data class MatchInput(
    val items: Set<MatchInputItem>,
)

data class MatchInputItem(
    val gameMember: GameMember,
    val rank: Int,
    val gameScore: Int,
) {
    val reliability: Double
        get() = max((10.0 - gameMember.matchCount) * 3 / 10, 1.0) // 1 ~ 3
}
