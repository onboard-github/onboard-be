package com.yapp.bol.admin.api.supervisor

import com.yapp.bol.admin.api.supervisor.dto.AdminPermissionListResponse
import com.yapp.bol.admin.supervisor.SupervisorService
import com.yapp.bol.auth.getSecurityUserIdOrThrow
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminSupervisorController(
    private val supervisorService: SupervisorService,
) {
    @GetMapping("/admin/v1/supervisor/permission")
    @PreAuthorize("isAuthenticated()")
    fun getAdminPermission(): AdminPermissionListResponse {
        val userId = getSecurityUserIdOrThrow()
        return AdminPermissionListResponse(supervisorService.getRoleList(userId))
    }
}
