package com.yapp.bol.admin.api.admin.dto

import com.yapp.bol.admin.AdminPermission
import com.yapp.bol.admin.AdminPermissionGroup
import com.yapp.bol.admin.AdminRole

data class AdminPermissionListResponse(
    val roleList: Set<AdminRole>,
) {
    val permissionList: List<AdminPermission> = roleList.flatMap { it.permissionSet }.distinct()
    val groupList: List<AdminPermissionGroupResponse> =
        roleList.flatMap { role -> role.permissionSet.map { it.group } }.distinct().map { it.toResponse() }
}

data class AdminPermissionGroupResponse(
    val label: String,
    val url: String,
)

fun AdminPermissionGroup.toResponse() = AdminPermissionGroupResponse(
    label = this.label,
    url = this.url,
)
