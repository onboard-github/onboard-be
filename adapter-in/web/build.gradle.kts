import com.epages.restdocs.apispec.gradle.OpenApi3Task
import groovy.lang.Closure
import io.swagger.v3.oas.models.servers.Server
import java.io.FileOutputStream
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.epages.restdocs-api-spec") version "0.19.2"
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    val springVersion by properties
    val swaggerUiVersion by properties

    implementation(project(":adapter-in:admin"))
    implementation(project(":adapter-in:support:auth"))
    implementation(project(":adapter-in:support:oas"))

    implementation(project(":support:logging"))
    implementation(project(":support:yaml"))
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-aop:$springVersion")

    testImplementation(project(":adapter-in:admin", "testArchive"))
    testImplementation(project(":adapter-in:support:auth", "testArchive"))
    testImplementation(project(":adapter-in:support:oas", "testArchive"))
    testImplementation(project(":domain", "testArchive"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> {
        enabled = true
        mainClass.set("com.yapp.bol.WebApplicationKt")
    }

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

    register<Exec>("generateRedoc") {
        dependsOn("openapi3")

        val openApi3Task = this@tasks.named<OpenApi3Task>("openapi3").get()

        commandLine("python3", "${project(":adapter-in:support:oas").projectDir}/a.py", "${openApi3Task.outputDirectory}/openapi3.yaml")

        standardOutput = FileOutputStream("${project.projectDir}/src/main/resources/static/redoc/openapi3.yaml")
    }
}

fun toServer(url: String): Closure<Server> = closureOf<Server> { this.url = url } as Closure<Server>
