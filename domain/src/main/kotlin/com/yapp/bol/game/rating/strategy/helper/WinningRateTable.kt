package com.yapp.bol.game.rating.strategy.helper

import com.yapp.bol.game.member.GameMemberId

class WinningRateTable(private val members: Collection<GameMemberId>) {
    private val map: Map<GameMemberId, MutableList<Double>> = members.associateWith {
        MutableList(members.size) { 0.0 }
    }

    fun get(memberId: GameMemberId, rank: Int): Double {
        return map[memberId]!![rank - 1]
    }

    fun add(memberId: GameMemberId, rank: Int, percentage: Double) {
        val list = map[memberId]!!
        list[rank - 1] = list[rank - 1] + percentage
    }
}
