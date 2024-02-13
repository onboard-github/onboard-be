import com.github.gradle.node.npm.task.NpmTask
import groovy.lang.Closure
import io.swagger.v3.oas.models.servers.Server
import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI
import org.springframework.boot.gradle.tasks.bundling.BootJar


plugins {
    id("com.github.node-gradle.node") version "7.0.2" // Node.js 플러그인 추가
    id("com.epages.restdocs-api-spec") version "0.18.0"
    id("org.hidetake.swagger.generator") version "2.19.2"
    id("org.springframework.boot")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
}

buildscript {
    dependencies {
        classpath("com.github.node-gradle:gradle-node-plugin:7.0.2")
    }
}

dependencies {
    val springVersion by properties
    implementation(project(":support:logging"))
    implementation(project(":support:yaml"))
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-aop:$springVersion")
//    implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")

    testImplementation(project(":domain", "testArchive"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.0")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.18.0")
    swaggerCodegen("io.swagger.codegen.v3:swagger-codegen-cli:3.0.42")
    swaggerUI("org.webjars:swagger-ui:4.18.2")
}

// Spring Boot 백엔드와 React 프론트엔드 경로 설정
val frontendDir = "${project.projectDir}/frontend"
val frontendBuildDir = "$frontendDir/build"

// Node.js 설정
node {
    version.set("20.11.0") // 사용할 Node.js 버전 지정
    npmVersion.set("10.2.4")
    download.set(true)
    nodeProjectDir.set(file("$frontendDir/node_modules"))
}

tasks.register<NpmTask>("buildReact") {
    workingDir.set(file(frontendDir))

    println(System.getenv())
    val phase = System.getenv("PHASE") ?: "local"
    args.addAll("run", "build-$phase")
}

tasks.register<Copy>("copyAdminWeb") {
    dependsOn("buildReact")

    into("src/main/resources/static/.")

    into("/admin") {
        from("frontend/build/.")
    }
}

tasks.withType<Jar> {
    enabled = true
}
tasks.withType<BootJar> {
    enabled = false
    mainClass.set("com.yapp.bol.AdminApplicationKt")
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
