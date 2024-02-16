package com.yapp.bol.admin

/**
 * AdminPermission 이 가장 작은 단위
 * AdminRole 은 AdminPermission 묶음
 * AdminUser에게는 AdminRole이 할당 되며, 여려개의 Role이 할당 될 수 있음
 *
 * AdminPermissionGroup 은 Permssion 종류를 구분해주는 단위, Role이랑 관계 없음
 * AdminPermissionGroup은 논리적인 단위로, 일단 어드민 메뉴 단위로 묶고자 함.
 */

enum class AdminRole(
    val description: String,
    val permissionSet: Set<AdminPermission>,
) {
    ADMIN(
        "어드민을 증명하는 기본 역할",
        AdminPermission.REQUEST_PERMISSION,
    ),
    GROUP_LIST_ALL(
        "그룹 목록 전체 권한",
        AdminPermission.VIEW_GROUP_LIST,
        AdminPermission.EDIT_GROUP_ACCESS_CODE,
        AdminPermission.DELETE_GROUP,
    ),
    ;

    constructor(description: String, vararg permissions: AdminPermission) : this(description, permissions.toSet())
}

enum class AdminPermission(
    val group: AdminPermissionGroup,
    val description: String,
) {
    REQUEST_PERMISSION(AdminPermissionGroup.ADMIN, "어드민 권한 요청을 할 수 있는 권한"),
    VIEW_GROUP_LIST(AdminPermissionGroup.GROUP_LIST, "그룹 조회 권한 (그룹 access code 포함)"),
    EDIT_GROUP_ACCESS_CODE(AdminPermissionGroup.GROUP_LIST, "그룹 Access Code 수정 권한"),
    DELETE_GROUP(AdminPermissionGroup.GROUP_LIST, "그룹 삭제 권한"),
}

enum class AdminPermissionGroup(
    val label: String,
    val url: String,
) {
    ADMIN("Admin", "/admin"),
    USER("User", "/user"),
    GROUP_LIST("Group", "/group-list"),
}
