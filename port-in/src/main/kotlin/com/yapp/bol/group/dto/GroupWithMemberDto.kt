package com.yapp.bol.group.dto

import com.yapp.bol.group.GroupId
import com.yapp.bol.group.member.MemberId

data class GroupWithMemberDto(
  val id: GroupId, // group id
  val name: String, // group name
  val description: String,
  val organization: String?,
  val profileImageUrl: String,
  val memberId: MemberId,
  val nickname: String,
  val matchCount: Long
)
