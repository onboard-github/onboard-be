package com.yapp.bol.group.member

import com.yapp.bol.auth.UserId

class OwnerMember(
    id: MemberId = MemberId(0),
    userId: UserId,
    nickname: String,
    level: Int = 0,
) : Member(
    id = id,
    userId = userId,
    nickname = nickname,
    level = level,
)
