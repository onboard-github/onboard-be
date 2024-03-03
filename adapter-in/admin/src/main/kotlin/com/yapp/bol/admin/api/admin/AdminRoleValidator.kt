package com.yapp.bol.admin.api.admin

import com.yapp.bol.NoPermissionException
import com.yapp.bol.admin.AdminPermission
import com.yapp.bol.admin.AdminService
import com.yapp.bol.auth.UserId
import org.springframework.stereotype.Component

@Component
class AdminRoleValidator(
    private val adminService: AdminService,
) {
    fun requiredHasRole(userId: UserId, permission: AdminPermission) {
        val roleList = adminService.getAvailableRoleList(userId)

        val hasRole = roleList.flatMap { it.permissionSet }.any { it == permission }

        if (hasRole.not()) throw NoPermissionException
    }
}
