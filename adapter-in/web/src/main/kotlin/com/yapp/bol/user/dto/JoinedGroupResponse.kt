package com.yapp.bol.user.dto

import com.yapp.bol.group.dto.GroupWithMemberDto
import com.yapp.bol.group.dto.JoinedGroupDto

data class JoinedGroupResponseV2(
    val contents: List<JoinedGroupDto>,
)

data class JoinedGroupResponse(
    val contents: List<GroupWithMemberDto>,
)
