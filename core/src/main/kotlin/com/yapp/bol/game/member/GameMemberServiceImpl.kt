package com.yapp.bol.game.member

import com.yapp.bol.game.GameId
import com.yapp.bol.game.GameService
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.MemberId
import com.yapp.bol.season.SeasonService
import com.yapp.bol.transaction.MyTransactional
import org.springframework.stereotype.Service

@Service
class GameMemberServiceImpl(
    private val gameMemberQueryRepository: GameMemberQueryRepository,
    private val gameMemberCommandRepository: GameMemberCommandRepository,
    private val gameService: GameService,
    private val seasonService: SeasonService,
) : GameMemberService {

    @MyTransactional
    override fun getOrCreateGameMember(
        groupId: GroupId,
        gameId: GameId,
        memberId: MemberId,
    ): GameMember {
        val gameMember = gameMemberQueryRepository.findGameMember(memberId, gameId, groupId)

        if (gameMember != null) {
            return gameMember
        }

        val season = seasonService.getOrCreateSeason(groupId)

        return gameMemberCommandRepository.createGameMember(
            GameMember.of(
                gameId = gameId,
                memberId = memberId,
                season = season,
            ),
            groupId = groupId,
        )
    }

    override fun getMatchCountByMemberId(memberId: MemberId): Long {
        return gameMemberQueryRepository.getMatchCount(memberId = memberId)
    }
}
