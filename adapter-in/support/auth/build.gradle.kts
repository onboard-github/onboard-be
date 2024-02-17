import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.epages.restdocs-api-spec") version "0.18.0"
    id("org.springframework.boot")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
}

dependencies {
    val springVersion by properties
    implementation(project(":support:logging"))
    implementation(project(":support:yaml"))
    implementation(project(":core"))
    implementation(project(":adapter-in:support:oas"))

    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    api("org.springframework.boot:spring-boot-starter-security:$springVersion")

    testImplementation(project(":adapter-in:support:oas", "testArchive"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
