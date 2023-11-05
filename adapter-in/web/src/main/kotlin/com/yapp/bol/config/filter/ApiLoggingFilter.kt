package com.yapp.bol.config.filter

import com.yapp.bol.utils.logger
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.util.ContentCachingResponseWrapper

@WebFilter(urlPatterns = ["/api/*", "/v1/*"])
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiLoggingFilter : Filter {
    private val logger = logger()

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) {
        val request = RequestLoggingWrapper(servletRequest as HttpServletRequest)
        val response = getResponseWrapper(servletResponse as HttpServletResponse)

        try {
            chain.doFilter(servletRequest, response)
        } finally {
            if (request.requestURL.startsWith("/v1/file").not()) {
                logger.info("${generateRequestLog(request)}\n${generateResponseLog(response)}")
            }
        }
    }

    private fun generateRequestLog(request: HttpServletRequest): String {
        val log = StringBuilder("[Req]\n${request.method} ${request.requestURI}")

        if (request.queryString.isNullOrBlank().not()) {
            log.append("?${request.queryString}")
        }

        request.headerNames.iterator().forEach {
            log.append("\n$it=${request.getHeader(it)}")
        }

        request.reader.lines().forEach {
            log.append("\n$it")
        }

        return log.toString()
    }

    private fun generateResponseLog(response: ContentCachingResponseWrapper): String {

        val responseCacheWrapperObject = getResponseWrapper(response)

        val responseStr = String(responseCacheWrapperObject.contentAsByteArray)
        responseCacheWrapperObject.copyBodyToResponse()

        return "[Res]\n${response.status}\n$responseStr"
    }

    private fun getResponseWrapper(response: HttpServletResponse): ContentCachingResponseWrapper =
        response as? ContentCachingResponseWrapper ?: ContentCachingResponseWrapper(response)
}
