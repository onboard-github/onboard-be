
import groovy.lang.Closure
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.epages.restdocs-api-spec") version "0.18.0"
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
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}

fun toServer(url: String): Closure<Server> = closureOf<Server> { this.url = url } as Closure<Server>
