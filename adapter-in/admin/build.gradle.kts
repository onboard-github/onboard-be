import groovy.lang.Closure
import io.swagger.v3.oas.models.servers.Server
import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI
import org.springframework.boot.gradle.tasks.bundling.BootJar


plugins {
    id("com.epages.restdocs-api-spec") version "0.18.0"
    id("org.hidetake.swagger.generator") version "2.19.2"
    id("org.springframework.boot")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
}

dependencies {
    val springVersion by properties
    implementation(project(":adapter-in:support:auth"))
    implementation(project(":adapter-in:support:oas"))

    implementation(project(":support:logging"))
    implementation(project(":support:yaml"))
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-aop:$springVersion")

    testImplementation(project(":adapter-in:support:auth", "testArchive"))
    testImplementation(project(":adapter-in:support:oas", "testArchive"))
    testImplementation(project(":domain", "testArchive"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
}

tasks.register<Copy>("copyAdminWeb") {
    into("src/main/resources/static/.")

    into("/admin") {
        from("frontend/build/.")
    }
}

tasks.withType<Jar> {
    dependsOn("copyAdminWeb")
    enabled = true
}
tasks.withType<BootJar> {
    enabled = false
//    mainClass.set("com.yapp.bol.AdminApplicationKt")
}

tasks {
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
