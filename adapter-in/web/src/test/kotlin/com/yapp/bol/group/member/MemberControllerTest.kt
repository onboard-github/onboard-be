package com.yapp.bol.group.member

import com.yapp.bol.CannotDeleteOwnerException
import com.yapp.bol.auth.UserId
import com.yapp.bol.base.ARRAY
import com.yapp.bol.base.BOOLEAN
import com.yapp.bol.base.ControllerTest
import com.yapp.bol.base.ENUM
import com.yapp.bol.base.NUMBER
import com.yapp.bol.base.OpenApiTag
import com.yapp.bol.base.STRING
import com.yapp.bol.game.member.GameMemberService
import com.yapp.bol.group.GroupId
import com.yapp.bol.group.GroupService
import com.yapp.bol.group.member.dto.AddGuestRequest
import com.yapp.bol.group.member.dto.JoinGroupRequest
import com.yapp.bol.group.member.dto.UpdateMemberInfoRequest
import com.yapp.bol.group.member.nickname.NicknameValidation
import com.yapp.bol.group.member.nickname.NicknameValidationReason
import com.yapp.bol.pagination.cursor.SimplePaginationCursorResponse
import io.mockk.every
import io.mockk.mockk

class MemberControllerTest : ControllerTest() {
    private val groupService: GroupService = mockk()
    private val memberService: MemberService = mockk()
    private val gameMemberService: GameMemberService = mockk()
    override val controller = MemberController(groupService, memberService, gameMemberService)

    init {
        test("멤버 닉네임 검사") {
            val groupId = GroupId(1)
            val nickname = "holden"

            every {
                memberService.validateMemberNickname(any(), any())
            } returns NicknameValidation(
                isAvailable = false,
                reason = NicknameValidationReason.DUPLICATED_NICKNAME
            )

            get("/api/v1/group/{groupId}/member/validateNickname", arrayOf(groupId.value)) {
                queryParam("nickname", nickname)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "member/{method-name}",
                        tag = OpenApiTag.MEMBER,
                        description = "맴버 닉네임 검사"
                    ),
                    pathParameters(
                        "groupId" type NUMBER means "그룹 ID",
                    ),
                    queryParameters(
                        "nickname" type STRING means "닉네임"
                    ),
                    responseFields(
                        "isAvailable" type BOOLEAN means "그룹 내에서 닉네임 사용 가능 여부",
                        "reason" type ENUM(NicknameValidationReason::class) means "닉네임 사용 불가능한 이유"
                    )
                )
        }

        test("맴버 목록 가져오기") {
            val groupId = GroupId(1)
            val userId = UserId(1)
            val size = 5
            val nickname = "검색"
            val cursor = "김김김"

            every { memberService.getMembers(any()) } returns SimplePaginationCursorResponse(
                contents = List(size) {
                    HostMember(
                        id = MemberId(it.toLong()),
                        userId = UserId(it.toLong()),
                        nickname = "닉네임$it",
                        level = 0,
                    )
                },
                cursor = cursor,
                hasNext = true,
            )

            get("/api/v1/group/{groupId}/member", arrayOf(groupId.value)) {
                authorizationHeader(userId)
                queryParam("size", size.toString())
                queryParam("nickname", nickname)
                queryParam("cursor", cursor)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "member/{method-name}",
                        tag = OpenApiTag.MEMBER,
                        description = "맴버 목록 가져오기"
                    ),
                    pathParameters(
                        "groupId" type NUMBER means "그룹 ID",
                    ),
                    queryParameters(
                        "size" type NUMBER means "받아오고자 하는 맴버 개수",
                        "cursor" type STRING means "Cursor 방식 Pagination, 전 요청 에서 받아온 cursor를 그대로 전달" isOptional true,
                        "nickname" type STRING means "검색하고자 하는 닉네임, null일 경우 전체 목록을 반환" isOptional true,
                        "role" type ENUM(MemberRole::class) means "검색하고자 하는 맴버 역할, null일 경우 전체 목록을 반환" isOptional true,
                    ),
                    responseFields(
                        "contents" type ARRAY means "맴버 목록",
                        "contents[].id" type NUMBER means "맴버 ID",
                        "contents[].role" type ENUM(MemberRole::class) means "맴버 종류 구분",
                        "contents[].nickname" type STRING means "맴버가 그룹에서 사용하는 닉네임",
                        "contents[].level" type NUMBER means "주사위 모양 데이터",
                        "cursor" type STRING means "다음 페이지를 가져오기 위한 기준 값",
                        "hasNext" type BOOLEAN means "다음 페이지 존재 여부",
                    )
                )
        }

        test("그룹 가입 - Host Member") {
            val groupId = GroupId(1)
            val userId = UserId(1)
            val request = JoinGroupRequest("nickname", "accessCode", null)

            every { groupService.joinGroup(any()) } returns Unit

            post("/api/v1/group/{groupId}/host", request, arrayOf(groupId.value)) {
                authorizationHeader(userId)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(identifier = "member/{method-name}", tag = OpenApiTag.MEMBER),
                    pathParameters(
                        "groupId" type NUMBER means "그룹 ID",
                    ),
                    requestFields(
                        "nickname" type STRING means "그룹 전용 닉네임, null 일 경우 유저 기본 닉네임을 사용" isOptional true,
                        "guestId" type STRING means "게스트 연동 할 ID, nickname보다 우선시 됩니다." isOptional true,
                        "accessCode" type STRING means "그룹에 가입하기 위한 참여 코드",
                    ),
                    responseFields()
                )
        }

        test("게스트 추가 - Host Member") {
            val groupId = GroupId(1)
            val userId = UserId(1)
            val request = AddGuestRequest("nickname")

            every { groupService.addGuest(any()) } returns Unit

            post("/api/v1/group/{groupId}/guest", request, arrayOf(groupId.value)) {
                authorizationHeader(userId)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(identifier = "member/{method-name}", tag = OpenApiTag.MEMBER),
                    pathParameters(
                        "groupId" type NUMBER means "그룹 ID",
                    ),
                    requestFields(
                        "nickname" type STRING means "그룹 전용 닉네임, null 일 경우 유저 기본 닉네임을 사용" isOptional false,
                    ),
                    responseFields()
                )
        }

        test("그룹 내 멤버의 플레이 횟수 가져오기 (없으면 0)") {
            val groupId = GroupId(1)
            val memberId = MemberId(1)

            every { gameMemberService.getMatchCountByMemberId(any()) } returns 1L

            get("/api/v1/group/{groupId}/member/{memberId}/match/count", arrayOf(groupId.value, memberId.value)) {}
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(identifier = "member/{method-name}", tag = OpenApiTag.MEMBER),
                    pathParameters(
                        "groupId" type NUMBER means "그룹 ID",
                        "memberId" type NUMBER means "멤버 ID",
                    ),
                    responseFields(
                        "matchCount" type NUMBER means "플레이 횟수",
                    )
                )
        }

        test("멤버 정보 변경") {
            val userId = UserId(1L)
            val groupId = GroupId(1L)
            val memberId = MemberId(1L)

            val newNickname = "new"

            val request = UpdateMemberInfoRequest(newNickname)

            every {
                memberService.updateMemberInfo(any(), any(), any())
            } returns HostMember(
                id = MemberId(1),
                userId = UserId(1),
                nickname = newNickname,
                level = 0,
            )

            patch(
                url = "/api/v1/group/{groupId}/member/{memberId}",
                pathParams = arrayOf(groupId.value, memberId.value),
                request = request
            ) {
                authorizationHeader(userId)
            }
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(identifier = "member/{method-name}", tag = OpenApiTag.MEMBER),
                    pathParameters(
                        "groupId" type NUMBER means "그룹 ID",
                        "memberId" type NUMBER means "멤버 ID",
                    ),
                    requestFields(
                        "nickname" type STRING means "변경할 닉네임",
                    ),
                    responseFields(
                        "id" type NUMBER means "맴버 ID",
                        "role" type ENUM(MemberRole::class) means "맴버 종류 구분",
                        "nickname" type STRING means "맴버가 그룹에서 사용하는 닉네임",
                        "level" type NUMBER means "주사위 모양 데이터",
                    )
                )
        }

        context("맴버 탈퇴") {
            test("맴버 탈퇴 - Host Member") {
                val groupId = GroupId(1)
                val userId = UserId(1)
                val request = AddGuestRequest("nickname")

                every { memberService.deleteMyMember(any(), any()) } returns Unit

                delete("/api/v1/group/{groupId}/me", request, arrayOf(groupId.value)) {
                    authorizationHeader(userId)
                }
                    .isStatus(200)
                    .makeDocument(
                        DocumentInfo(identifier = "member/{method-name}", tag = OpenApiTag.MEMBER),
                        pathParameters(
                            "groupId" type NUMBER means "그룹 ID",
                        ),
                        requestFields(
                            "nickname" type STRING means "그룹 전용 닉네임, null 일 경우 유저 기본 닉네임을 사용" isOptional false,
                        ),
                        responseFields()
                    )
            }
            test("맴버 탈퇴 - Owner Member") {
                val groupId = GroupId(1)
                val userId = UserId(1)
                val request = AddGuestRequest("nickname")

                every { memberService.deleteMyMember(any(), any()) } throws CannotDeleteOwnerException

                delete("/api/v1/group/{groupId}/me", request, arrayOf(groupId.value)) {
                    authorizationHeader(userId)
                }
                    .isStatus(400)
                    .makeDocument(
                        DocumentInfo(identifier = "member/{method-name}", tag = OpenApiTag.MEMBER),
                        pathParameters(
                            "groupId" type NUMBER means "그룹 ID",
                        ),
                        requestFields(
                            "nickname" type STRING means "그룹 전용 닉네임, null 일 경우 유저 기본 닉네임을 사용" isOptional false,
                        ),
                        responseFields(
                            "code" type STRING means "에러 코드",
                            "message" type STRING means "에러메시지",
                        )
                    )
            }
        }
    }
}
