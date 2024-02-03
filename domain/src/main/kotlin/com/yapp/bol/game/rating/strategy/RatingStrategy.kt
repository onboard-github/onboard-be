package com.yapp.bol.game.rating.strategy

import com.yapp.bol.game.member.GameMember
import com.yapp.bol.game.rating.dto.MatchInput

interface RatingStrategy {
    fun compute(input: MatchInput): List<GameMember>
}
