
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
    val swaggerUiVersion by properties

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
    swaggerUI("org.webjars:swagger-ui:$swaggerUiVersion")
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
