package com.yapp.bol.admin

import com.yapp.bol.admin.configuration.AdminResourceResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(
    private val adminResourceResolver: AdminResourceResolver,
    @Value("\${bol.server.host}") private val host: String,
) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/admin/static/")

        registry.addResourceHandler("/bgbg", "/bgbg/**")
            .addResourceLocations("classpath:/static/admin/index.html")
            .setCachePeriod(0)
            .resourceChain(true)
            .addResolver(adminResourceResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        val origins =
            arrayOf("http://localhost:3000", "http://localhost:8080", host)
        registry.addMapping("/admin/**").allowedOrigins(*origins).allowedMethods("*")
        registry.addMapping("/api/v1/auth/**").allowedOrigins(*origins).allowedMethods("*")
    }
}
