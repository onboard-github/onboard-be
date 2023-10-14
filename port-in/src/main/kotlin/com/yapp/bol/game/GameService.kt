package com.yapp.bol.game

import com.yapp.bol.group.GroupId

interface GameService {
    fun getGameList(groupId: GroupId, sortType: GameListSortType): List<GameWithMatchCount>

    fun validateMemberSize(gameId: GameId, memberCount: Int): Boolean
}
