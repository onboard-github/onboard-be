package com.yapp.bol

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer{
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        println("Configure")
        registry.addResourceHandler("/bgbg/**")
            .addResourceLocations("classpath:/static/admin/")

        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/admin/static/")
    }
}
