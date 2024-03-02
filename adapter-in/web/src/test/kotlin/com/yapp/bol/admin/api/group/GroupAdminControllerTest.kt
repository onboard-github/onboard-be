package com.yapp.bol.admin.api.group

import com.yapp.bol.base.ARRAY
import com.yapp.bol.base.BaseControllerTest
import com.yapp.bol.base.NUMBER
import com.yapp.bol.base.OpenApiTag
import com.yapp.bol.base.STRING
import com.yapp.bol.file.MockFileInfo
import com.yapp.bol.group.Group
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.GroupService
import com.yapp.bol.group.dto.GroupWithMemberCount
import com.yapp.bol.pagination.offset.PaginationOffsetResponse
import io.mockk.every
import io.mockk.mockk

open class GroupAdminControllerTest : BaseControllerTest() {
    private val groupService: GroupService = mockk()
    override val controller = GroupAdminController(groupService)

    init {
        test("그룹 목록 가져오기 (어드민)") {
            every {
                groupService.searchGroup(any(), any(), any())
            } returns PaginationOffsetResponse(
                content = listOf(GROUP_WITH_MEMBER_COUNT),
                totalCount = 30,
                hasNext = false,
            )

            get("/admin/v1/group-list") {}
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "admin_group/{method-name}",
                        description = "그룹 목록 검색하기",
                        tag = OpenApiTag.ADMIN_GROUP,
                    ),
                    queryParameters(
                        "keyword" type STRING means "검색하고자 하는 텍스트, (이름/소속)을 검색합니다. (디폴트 All)" isOptional true,
                        "pageNumber" type NUMBER means "페이지 번호 (디폴트 0)" isOptional true,
                        "pageSize" type NUMBER means "페이지 크기 (디폴트 10)" isOptional true,
                    ),
                    responseFields(
                        "list" type ARRAY means "그룹 목록",
                        "list[].id" type NUMBER means "그룹 ID",
                        "list[].name" type STRING means "그룹 이름",
                        "list[].description" type STRING means "그룹 설명",
                        "list[].organization" type STRING means "그룹 소속" isOptional true,
                        "list[].accessCode" type STRING means "그룹 참여 코드",
                        "list[].memberCount" type NUMBER means "그룹 멤버 수",
                        "totalCount" type NUMBER means "전체 그룹 개수",
                    ),
                )
        }

    }


    companion object {
        val GROUP = Group(
            id = GroupId(1),
            name = "뽀글뽀글",
            description = "보겜동입니다",
            organization = "카카오",
            profileImage = MockFileInfo(),
            accessCode = "1A2B3C",
        )

        val GROUP_WITH_MEMBER_COUNT = GroupWithMemberCount(
            group = GROUP,
            memberCount = 30,
        )
    }
}
