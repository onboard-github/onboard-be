package com.yapp.bol.admin

import com.yapp.bol.admin.configuration.AdminResourceResolver
import org.springframework.context.annotation.Configuration
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
}
