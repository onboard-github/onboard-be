package com.yapp.bol.configuration

import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.web.servlet.resource.PathResourceResolver

@Component
class AdminResourceResolver : PathResourceResolver() {
    override fun getResource(resourcePath: String, location: Resource): Resource? {
        if (
            resourcePath.startsWith("api") || resourcePath.startsWith("/api") ||
            resourcePath.startsWith("admin") || resourcePath.startsWith("/admin")
        ) {
            return null
        }

        if (location.exists() && location.isReadable) return location

        return null
    }
}
