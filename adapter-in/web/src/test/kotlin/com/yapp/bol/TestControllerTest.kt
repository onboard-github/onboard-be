package com.yapp.bol

import com.yapp.bol.base.NUMBER
import com.yapp.bol.base.OpenApiTag
import com.yapp.bol.base.STRING
import com.yapp.bol.base.WebControllerTest
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.env.Environment

class TestControllerTest : WebControllerTest() {
    private val environment: Environment = mockk()

    override val controller = TestController(environment)

    init {
        test("Get Test") {
            every { environment.activeProfiles } returns arrayOf("phase")

            get("/api/v1/test") {}
                .isStatus(200)
                .makeDocument(
                    DocumentInfo(identifier = "test", tag = OpenApiTag.TEST),
                    responseFields(
                        "value" type STRING means "English??",
                        "test" type NUMBER means "이건 몬지몰라" isOptional true,
                    ),
                )
        }
    }
}
