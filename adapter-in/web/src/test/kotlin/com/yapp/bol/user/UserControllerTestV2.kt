package com.yapp.bol.user

import com.yapp.bol.auth.UserId
import com.yapp.bol.base.ARRAY
import com.yapp.bol.base.ControllerTest
import com.yapp.bol.base.NUMBER
import com.yapp.bol.base.OpenApiTag
import com.yapp.bol.base.STRING
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.GroupService
import com.yapp.bol.group.dto.JoinedGroupDto
import io.mockk.every
import io.mockk.mockk

class UserControllerTestV2 : ControllerTest() {
    private val groupService: GroupService = mockk()
    override val controller: Any
        get() = UserControllerV2(groupService)

    init {
        test("내가 가입한 그룹 목록 가져오기 v2") {
            val user = User(
                id = UserId(2220),
                nickname = "닉네임",
            )

            every { groupService.getJoinedGroups(user.id) } returns listOf(
                JoinedGroupDto(
                    groupId = GroupId(1),
                    groupName = "그룹 이름",
                    nickname = "닉네임",
                    organization = "소속",
                    matchCount = 10L,
                )
            )

            get("/api/v2/user/me/group") {
                authorizationHeader(user.id)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "user/{method-name}",
                        tag = OpenApiTag.USER,
                        description = "내가 가입한 그룹 목록 가져오기 v2"
                    ),
                    responseFields(
                        "contents" type ARRAY means "그룹 목록",
                        "contents[].groupId" type NUMBER means "그룹 아이디",
                        "contents[].groupName" type STRING means "그룹 이름",
                        "contents[].nickname" type STRING means "멤버 이름",
                        "contents[].organization" type STRING means "그룹 소속",
                        "contents[].matchCount" type NUMBER means "그룹 내 플레이한 게임 수",
                    )
                )
        }
    }
}
