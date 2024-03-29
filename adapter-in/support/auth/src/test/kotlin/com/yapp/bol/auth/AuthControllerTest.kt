package com.yapp.bol.auth

import com.yapp.bol.auth.dto.LoginRequest
import com.yapp.bol.base.BaseControllerTest
import com.yapp.bol.base.ENUM
import com.yapp.bol.base.OpenApiTag
import com.yapp.bol.base.STRING
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

open class AuthControllerTest : BaseControllerTest(emptyList()) {
    private val authService: AuthService = mockk()
    override val controller = AuthController(authService)

    init {
        test("POST /api/v1/auth/login") {
            val request = LoginRequest(LoginType.KAKAO_ACCESS_TOKEN, "Token")
            val userId = UserId(123L)
            val authToken = AuthToken(
                Token("ACCESS_TOKEN", userId, LocalDateTime.now()),
                Token("REFRESH_TOKEN", userId, LocalDateTime.now()),
            )
            every { authService.login(any(), any()) } returns authToken

            post("/api/v1/auth/login", request) {}
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(
                        identifier = "test",
                        tag = OpenApiTag.AUTH,
                        description = "로그인",
                    ),
                    requestFields(
                        "type" type ENUM(LoginType::class) means "로그인 방법",
                        "token" type STRING means "로그인에 사용되는 토큰",
                    ),
                    responseFields(
                        "accessToken" type STRING means "Access 토큰",
                        "refreshToken" type STRING means "Refresh 토큰" isOptional true,
                    ),
                )
        }
    }
}
