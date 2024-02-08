import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    val springVersion by properties

    api(project(":domain"))
    api(project(":port-out"))
    implementation(project(":support:logging"))
    implementation(project(":support:yaml"))

    implementation("org.springframework.boot:spring-boot-starter-webflux:$springVersion")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
