package com.yapp.bol.notify

import com.yapp.bol.group.Group
import com.yapp.bol.match.Match

sealed class DeveloperNotifyType(
    val message: String,
    val description: String,
) {

    data class CREATE_MATCH(
        val match: Match,
        val group: Group,
    ) : DeveloperNotifyType(
        """Match 생성 호출! (MatchId: ${match.id})
            그룹 이름(ID): ${group.name} (${group.id})
            매치 인원수: ${match.memberCount}
            매치 게임 ID: ${match.gameId}
        """.trimIndent(),
        "Match 생성 알림",
    )
}
