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
}
