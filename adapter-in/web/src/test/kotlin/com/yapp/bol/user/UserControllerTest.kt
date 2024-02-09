package com.yapp.bol.user

import com.yapp.bol.NotDeleteUserByOwnerException
import com.yapp.bol.auth.UserId
import com.yapp.bol.base.ARRAY
import com.yapp.bol.base.ControllerTest
import com.yapp.bol.base.NUMBER
import com.yapp.bol.base.OpenApiTag
import com.yapp.bol.base.STRING
import com.yapp.bol.file.MockFileInfo
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.GroupService
import com.yapp.bol.group.dto.GroupWithMemberDto
import com.yapp.bol.group.member.MemberId
import com.yapp.bol.onboarding.OnboardingGuide
import com.yapp.bol.onboarding.OnboardingService
import com.yapp.bol.onboarding.OnboardingType
import com.yapp.bol.user.dto.PutUserInfoRequest
import io.mockk.every
import io.mockk.mockk

class UserControllerTest : ControllerTest() {
    private val userService: UserService = mockk()
    private val groupService: GroupService = mockk()
    private val onboardingService: OnboardingService = mockk()
    override val controller: Any
        get() = UserController(userService, groupService, onboardingService)

    init {
        test("온보딩 진행 정도 가져오기") {
            val userId = UserId(124)
            every { onboardingService.getRemainOnboarding(userId) } returns OnboardingGuide(
                listOf(
                    OnboardingType.TERMS,
                    OnboardingType.NICKNAME,
                ),
                GroupId(1),
            )

            get("/api/v1/user/me/onboarding") {
                authorizationHeader(userId)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "user/{method-name}",
                        tag = OpenApiTag.USER,
                        description = "온보딩 진행 정도 가져오기",
                    ),
                    responseFields(
                        "onboarding" type ARRAY means "남은 온보딩 단계 ${OnboardingType.values().toList()}",
                        "mainGroupId" type NUMBER means "홈에 보여줄 그룹 ID, 가입한 그룹이 없을 경우 NULL" isOptional true,
                    ),
                )
        }

        test("내 기본 정보 가져오기") {
            val user = User(
                id = UserId(2220),
                nickname = "닉네임",
            )
            every { userService.getUser(user.id) } returns user

            get("/api/v1/user/me") {
                authorizationHeader(user.id)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "user/{method-name}",
                        tag = OpenApiTag.USER,
                        description = "내 기본 정보 가져오기",
                    ),
                    responseFields(
                        "id" type NUMBER means "유저 아이디",
                        "nickname" type STRING means "유저 기본 닉네임" isOptional true,
                    ),
                )
        }

        test("내가 가입한 그룹 목록 가져오기") {
            val user = User(
                id = UserId(2220),
                nickname = "닉네임",
            )

            every { groupService.getGroupWithMemberInfo(user.id) } returns listOf(
                GroupWithMemberDto(
                    id = GroupId(1),
                    name = "그룹 이름",
                    description = "그룹 설명",
                    organization = "그룹 소속",
                    profileImageUrl = MockFileInfo().getUrl(),
                    nickname = "닉네임",
                    matchCount = 10L,
                    memberId = MemberId(1),
                ),
            )

            get("/api/v1/user/me/group") {
                authorizationHeader(user.id)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "user/{method-name}",
                        tag = OpenApiTag.USER,
                        description = "내가 가입한 그룹 목록 가져오기 v1 (추후 변경 예정)",
                    ),
                    responseFields(
                        "contents" type ARRAY means "그룹 목록",
                        "contents[].id" type NUMBER means "그룹 ID",
                        "contents[].name" type STRING means "그룹명",
                        "contents[].description" type STRING means "그룹 소개",
                        "contents[].organization" type STRING means "그룹 소속" isOptional true,
                        "contents[].profileImageUrl" type STRING means "그룹 이미지 URL",
                        "contents[].memberId" type NUMBER means "멤버 ID",
                        "contents[].nickname" type STRING means "멤버 이름",
                        "contents[].matchCount" type NUMBER means "그룹 내 플레이한 게임 수",
                    ),
                )
        }

        test("유저 전체 정보 업데이트") {
            val userId = UserId(123L)
            val user = PutUserInfoRequest(
                nickname = "닉네임",
            )

            every { userService.putUser(any()) } returns Unit

            put("/api/v1/user/me", user) {
                authorizationHeader(userId)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "user/{method-name}",
                        tag = OpenApiTag.USER,
                        description = "유저 전체 정보 업데이트",
                    ),
                    requestFields(
                        "nickname" type STRING means "수정하고자 하는 닉네임",
                    ),
                )
        }

        test("내가 플레이한 게임 수 가져오기 (없으면 0)") {
            val userId = UserId(123L)
            every { userService.getMatchCountByUserId(userId) } returns 10L

            get("/api/v1/user/me/match/count") {
                authorizationHeader(userId)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "user/{method-name}",
                        description = "내가 플레이한 게임 수 가져오기 (없으면 0)",
                        tag = OpenApiTag.USER,
                    ),
                    responseFields(
                        "matchCount" type NUMBER means "플레이한 게임 수",
                    ),
                )
        }

        context("유저 탈퇴") {
            test("성공") {
                val userId = UserId(123L)

                every { userService.deleteUser(any()) } returns Unit

                delete("/api/v1/user/me") {
                    authorizationHeader(userId)
                }
                    .isStatus(200)
                    .makeDocument(
                        DocumentInfo(identifier = "user/{method-name}", tag = OpenApiTag.USER),
                    )
            }
            test("NotDeleteUserByOwnerException") {
                val userId = UserId(123L)

                every { userService.deleteUser(any()) } throws NotDeleteUserByOwnerException

                delete("/api/v1/user/me") {
                    authorizationHeader(userId)
                }
                    .isStatus(400)
                    .makeDocument(
                        DocumentInfo(identifier = "user/{method-name}", tag = OpenApiTag.USER),
                        responseFields(
                            "code" type STRING means "에러 코드",
                            "message" type STRING means "에러메시지",
                        ),
                    )
            }
        }
    }
}
