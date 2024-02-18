package com.yapp.bol.admin.api.admin

import com.yapp.bol.admin.AdminService
import com.yapp.bol.admin.api.admin.dto.AdminPermissionListResponse
import com.yapp.bol.admin.api.admin.dto.NewPermissionRequest
import com.yapp.bol.auth.getSecurityUserIdOrThrow
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminRoleController(
    private val adminService: AdminService,
) {
    @GetMapping("/admin/v1/admin/permission")
    @PreAuthorize("isAuthenticated()")
    fun getAdminRole(): AdminPermissionListResponse {
        val userId = getSecurityUserIdOrThrow()
        return AdminPermissionListResponse(adminService.getAvailableRoleList(userId))
    }

    @PostMapping("/admin/v1/admin/permission")
    @PreAuthorize("isAuthenticated()")
    fun requestRole(
        @RequestBody request: NewPermissionRequest,
    ) {
        val userId = getSecurityUserIdOrThrow()
        adminService.requestRole(userId, request.role, request.memo)
    }
}
