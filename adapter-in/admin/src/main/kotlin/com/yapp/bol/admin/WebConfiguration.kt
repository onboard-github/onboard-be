package com.yapp.bol.admin

import com.yapp.bol.admin.configuration.AdminResourceResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(
    private val adminResourceResolver: AdminResourceResolver,
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
            arrayOf("http://localhost:3000", "http://localhost:8080", "http://*.onboardgame.co.kr", "https://*.onboardgame.co.kr")
        registry.addMapping("/admin/**").allowedOrigins(*origins)
        registry.addMapping("/api/v1/auth/**").allowedOrigins(*origins)
    }
}
