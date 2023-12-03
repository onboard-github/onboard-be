package com.yapp.bol.group.member

import com.yapp.bol.auth.UserId

class HostMember(
    id: MemberId = MemberId(0),
    userId: UserId,
    nickname: String,
    level: Int = 0,
) : ParticipantMember(
    id = id,
    userId = userId,
    nickname = nickname,
    level = level,
) {
    override fun changeNickname(nickname: String): Member {
        return HostMember(
            id = id,
            userId = this.userId!!,
            nickname = nickname,
            level = level,
        )
    }
}
