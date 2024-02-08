package com.yapp.bol.user

import com.yapp.bol.auth.getSecurityUserIdOrThrow
import com.yapp.bol.group.GroupService
import com.yapp.bol.user.dto.JoinedGroupResponseV2
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v2/user", "/api/v2/user")
@RestController
class UserControllerV2(
    private val groupService: GroupService,
) {
    @Deprecated("기존 버전으로 롤백")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/group")
    fun getJoinedGroups(): JoinedGroupResponseV2 {
        val userId = getSecurityUserIdOrThrow()

        val joinedGroupDtos = groupService.getJoinedGroupsForV2(userId)

        return JoinedGroupResponseV2(
            contents = joinedGroupDtos,
        )
    }
}
