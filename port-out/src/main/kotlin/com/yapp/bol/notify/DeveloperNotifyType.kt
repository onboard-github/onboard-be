package com.yapp.bol.notify

import com.yapp.bol.group.GroupId
import com.yapp.bol.match.MatchId

sealed class DeveloperNotifyType(val description: String) {
    data class CREATE_MATCH(
        val matchId: MatchId,
        val groupId: GroupId,
        val groupName: String,
    ) : DeveloperNotifyType("Match 생성 알림")
}
