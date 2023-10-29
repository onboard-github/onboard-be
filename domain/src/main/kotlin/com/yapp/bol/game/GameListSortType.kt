package com.yapp.bol.game

enum class GameListSortType(val description: String) {
    FIXED("서버에서 인위적으로 정해진 순서"),
    MATCH_COUNT("그룹 내 가장 많이 플레이한 게임 순"),
}
