package com.yapp.bol.group.member

import com.yapp.bol.NoPermissionException
import com.yapp.bol.auth.UserId
import com.yapp.bol.group.GroupId
import org.springframework.stereotype.Component

@Component
class MemberValidator(
    private val memberService: MemberService,
) {
    fun requiredGroupOwner(groupId: GroupId, userId: UserId) {
        val member = memberService.findMemberByGroupIdAndUserId(groupId, userId)

        if (member == null || member.isOwner().not()) throw NoPermissionException
    }
}
