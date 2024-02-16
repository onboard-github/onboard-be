package com.yapp.bol.admin.api.supervisor.dto

import com.yapp.bol.admin.supervisor.AdminPermission
import com.yapp.bol.admin.supervisor.AdminPermissionGroup
import com.yapp.bol.admin.supervisor.AdminRole

data class AdminPermissionListResponse(
    val roleList: List<AdminRole>,
) {
    val permissionList: List<AdminPermission> = roleList.flatMap { it.permissionSet }
    val groupList: List<AdminPermissionGroupResponse> =
        roleList.flatMap { role -> role.permissionSet.map { it.group } }.distinct().map { it.toResponse() }
}

data class AdminPermissionGroupResponse(
    val label: String,
    val url: String,
)

fun AdminPermissionGroup.toResponse() = AdminPermissionGroupResponse(
    label = this.label,
    url = this.url
)
