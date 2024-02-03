package com.yapp.bol.game.rating.strategy.helper

import com.yapp.bol.game.member.GameMemberId
import kotlin.math.pow

class ExponentialCalculator(
    private val a: Int, // k 점수 차이가 나면 승률 = a : 1
    private val k: Int, // 점수 차이
) {

    fun calcWinningRate(scores: Map<GameMemberId, Int>): WinningRateTable {
        val result = WinningRateTable(scores.keys)

        val abilities = scores.map { it.key to calcAbility(it.value) }

        calc(result, 1, CalcItem(abilities))

        return result
    }

    private fun calcAbility(score: Int): Double {
        return a.toDouble().pow(score.toDouble() / k)
    }

    private fun calc(table: WinningRateTable, rank: Int, data: CalcItem) {
        if (data.abilities.size == 1) {
            val pair = data.abilities[0]
            table.add(pair.first, rank, data.posPercentage)
            return
        }

        data.abilities.forEach {
            val percentage = it.second / data.totalAbilities * data.posPercentage
            table.add(it.first, rank, percentage)
            calc(table, rank + 1, data.next(it.first, percentage))
        }
    }

    internal data class CalcItem(
        val abilities: List<Pair<GameMemberId, Double>>,
        val posPercentage: Double = 1.0,
    ) {
        val totalAbilities: Double = abilities.sumOf { it.second }
        fun next(ignoreUser: GameMemberId, percentage: Double): CalcItem =
            this.copy(abilities = abilities.filterNot { it.first == ignoreUser }, posPercentage = percentage)
    }
}
