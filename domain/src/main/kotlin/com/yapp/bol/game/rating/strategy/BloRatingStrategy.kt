package com.yapp.bol.game.rating.strategy

import com.yapp.bol.game.member.GameMember
import com.yapp.bol.game.rating.dto.MatchInput
import com.yapp.bol.game.rating.strategy.helper.ExponentialCalculator
import kotlin.math.min

object BloRatingStrategy : RatingStrategy {

    override fun compute(input: MatchInput): List<GameMember> {
        val calculator = ExponentialCalculator(9, 1000)
        val winningRate = calculator.calcWinningRate(
            input.items.associate {
                it.gameMember.id to it.gameMember.finalScore
            }
        )

        val gameReliability = input.items.maxOf { it.reliability }
        return input.items.map { item ->
            val member = item.gameMember
            val rank = item.rank
            val size = input.items.size

            val winningRateScore = 1 - winningRate.get(member.id, rank) // 0 ~ 1
            var rankScore = 2.0 / (size - 1) * (size - rank) - 1 // -1 ~ 1
            if (rankScore >= 0 && rankScore < 0.1) rankScore = 0.1
            val reliabilityScore = item.reliability * item.reliability / gameReliability // 0.3 ~ 3

            var deltaScore = 50.0 * winningRateScore * rankScore * reliabilityScore

            if (member.finalScore < 300 && member.matchCount <= 7) {
                if (deltaScore <= 0)
                    deltaScore = 0.0
                else
                    deltaScore += 40 * reliabilityScore
            } else if (member.finalScore < 400 && member.matchCount <= 15) {
                if (deltaScore <= 0)
                    deltaScore *= 0.3
                else
                    deltaScore += 30 * reliabilityScore
            } else if (member.finalScore < 500 && member.matchCount <= 15) {
                if (deltaScore <= 0)
                    deltaScore *= 0.7
                else
                    deltaScore += 20 * reliabilityScore
            } else if (deltaScore < 0 && member.finalScore + deltaScore < 100) {
                deltaScore *= 0.2
            }

            if (deltaScore > 0) {
                deltaScore += min(deltaScore * 0.2, 3.0)
            }

            member.updateScore(deltaScore.toInt())
        }
    }
}
