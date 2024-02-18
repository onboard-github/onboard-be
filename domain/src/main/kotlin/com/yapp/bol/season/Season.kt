package com.yapp.bol.season

import com.yapp.bol.group.GroupId

@JvmInline
value class SeasonId(val value: Long) {
    override fun toString(): String = value.toString()
}

data class Season(
    val id: SeasonId = SeasonId(0),
    val groupId: GroupId,
)
