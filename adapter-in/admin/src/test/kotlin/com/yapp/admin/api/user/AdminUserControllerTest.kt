package com.yapp.admin.api.user

import com.yapp.bol.admin.AdminRole
import com.yapp.bol.admin.AdminService
import com.yapp.bol.admin.api.admin.AdminController
import com.yapp.bol.auth.UserId
import com.yapp.bol.auth.authorizationHeader
import com.yapp.bol.base.ARRAY
import com.yapp.bol.base.BaseControllerTest
import com.yapp.bol.base.OpenApiTag
import io.mockk.every
import io.mockk.mockk

class AdminUserControllerTest : BaseControllerTest() {
    val adminUserService: AdminService = mockk()
    override val controller = AdminController(adminUserService)

    init {
        test("어드민의 권한 목록 가져오기") {
            val userId = UserId(1)

            every { adminUserService.getRoleList(userId) } returns listOf(AdminRole.GROUP_LIST_ALL)

            get("/admin/v1/admin/permission") {
                authorizationHeader(userId)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "admin/user/{method-name}",
                        tag = OpenApiTag.ADMIN_BASE,
                        description = "어드민의 권한 목록 가져오기",
                    ),
                    responseFields(
                        "roleList" type ARRAY means "Role 목록",
                        "permissionList" type ARRAY means "권한 ID",
                        "groupList" type ARRAY means "권한 그룹 목록",
                    ),
                )
        }
    }
}
