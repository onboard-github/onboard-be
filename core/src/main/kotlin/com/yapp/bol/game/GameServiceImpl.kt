package com.yapp.bol.game

import com.yapp.bol.NotFoundGameException
import com.yapp.bol.group.GroupId
import org.springframework.stereotype.Service

@Service
internal class GameServiceImpl(
    private val gameQueryRepository: GameQueryRepository,
) : GameService {
    override fun getGameList(groupId: GroupId, sortType: GameListSortType): List<GameWithMatchCount> {
        val list = gameQueryRepository.getGameListByGroupId(groupId)
        if (sortType == GameListSortType.FIXED) return list

        return list.sortedByDescending { it.matchCount }
    }

    override fun validateMemberSize(gameId: GameId, memberCount: Int): Boolean {
        val game = gameQueryRepository.findById(gameId) ?: throw NotFoundGameException

        return memberCount in game.minMember..game.maxMember
    }
}
