package com.yapp.bol.game.member

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yapp.bol.group.member.MemberId

class CustomGameMemberRepositoryImpl(
    val queryFactory: JPAQueryFactory,
) : CustomGameMemberRepository {
    override fun getMatchCount(memberId: MemberId): Long {
        val gameMember = QGameMemberEntity.gameMemberEntity

        val express = gameMember.memberId.eq(memberId.value)

        return queryFactory.select(gameMember.matchCount.sum())
            .from(gameMember)
            .where(express)
            .fetchOne()
            ?.toLong() ?: 0L
    }

    override fun getMatchCounts(memberIds: List<MemberId>): Map<MemberId, Long> {
        val gameMember = QGameMemberEntity.gameMemberEntity

        val conditions = gameMember.memberId.`in`(memberIds.map { it.value })

        return queryFactory
            .select(gameMember.memberId, gameMember.matchCount.sum())
            .from(gameMember)
            .where(conditions)
            .groupBy(gameMember.memberId)
            .fetch()
            .associate { result ->
                val memberId = MemberId(result.get(gameMember.memberId)!!)
                val matchCount = result.get(gameMember.matchCount.sum())?.toLong() ?: 0L

                memberId to matchCount
            }
    }
}
