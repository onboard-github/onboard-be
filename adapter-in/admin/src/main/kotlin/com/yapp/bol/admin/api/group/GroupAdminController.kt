package com.yapp.bol.admin.api.group

import com.yapp.bol.admin.AdminPermission
import com.yapp.bol.admin.EmptyResponse
import com.yapp.bol.admin.api.admin.AdminRoleValidator
import com.yapp.bol.admin.api.group.dto.GroupListResponse
import com.yapp.bol.admin.api.group.dto.toResponse
import com.yapp.bol.auth.getSecurityUserIdOrThrow
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.GroupService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupAdminController(
    private val adminRoleValidator: AdminRoleValidator,
    private val groupService: GroupService,
) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/admin/v1/group-list")
    fun getGroupList(
        @RequestParam keyword: String?,
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
    ): GroupListResponse {
        val userId = getSecurityUserIdOrThrow()
        adminRoleValidator.requiredHasRole(userId, AdminPermission.VIEW_GROUP_LIST)

        val list = groupService.searchGroup(keyword, pageNumber, pageSize)
        return GroupListResponse(
            list = list.content.map { it.toResponse() },
            totalCount = list.totalCount,
        )
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/admin/v1/group")
    fun deleteGroup(
        @RequestParam groupId: GroupId,
    ): EmptyResponse {
        val userId = getSecurityUserIdOrThrow()
        adminRoleValidator.requiredHasRole(userId, AdminPermission.DELETE_GROUP)

        groupService.deleteGroup(groupId)

        return EmptyResponse
    }
}
