package com.yapp.bol.auth

import com.yapp.bol.auth.security.TokenAuthentication
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

fun MockHttpServletRequestBuilder.authorizationHeader(userId: UserId) {
    org.springframework.security.core.context.SecurityContextHolder.getContext().authentication = TokenAuthentication("Token", userId)

    this.header("Authorization", "Bearer Token")
}
