import groovy.lang.Closure
import io.swagger.v3.oas.models.servers.Server
import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.epages.restdocs-api-spec") version "0.18.0"
    id("org.hidetake.swagger.generator") version "2.19.2"
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    val springVersion by properties
    implementation(project(":support:yaml"))
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
    testApi("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.0")
    testApi("com.epages:restdocs-api-spec-mockmvc:0.18.0")
    swaggerCodegen("io.swagger.codegen.v3:swagger-codegen-cli:3.0.42")
    swaggerUI("org.webjars:swagger-ui:4.18.2")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }

    val generateSwaggerUIPrefix = "Api"
    openapi3 {
        setServers(
            listOf(
                toServer("http://sandbox-api.onboardgame.co.kr"),
                toServer("http://api.onboardgame.co.kr"),
            ),
        )
        title = "온보드 API"
        description = "온보드 클라이언트에게 제공하는 API"
//    tagDescriptionsPropertiesFile = "src/docs/tag-descriptions.yaml"
        version = "0.1.0"
        format = "yaml"
    }

    swaggerSources {
        create(generateSwaggerUIPrefix).apply {
            setInputFile(file("${project.buildDir}/api-spec/openapi3.yaml"))
        }
    }
    withType<GenerateSwaggerUI> {
        dependsOn("openapi3")
    }
    register<Copy>("copySwaggerUI") {
        dependsOn("generateSwaggerUI$generateSwaggerUIPrefix")

        val generateSwaggerUISampleTask =
            this@tasks.named<GenerateSwaggerUI>("generateSwaggerUI$generateSwaggerUIPrefix").get()

        from("${generateSwaggerUISampleTask.outputDir}")
        into("src/main/resources/static/swagger")
    }
}

fun toServer(url: String): Closure<Server> = closureOf<Server> { this.url = url } as Closure<Server>