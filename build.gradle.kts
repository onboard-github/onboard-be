import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.springframework.boot")
}

repositories {
    mavenCentral()
    google()
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        val kotestVersion: String by properties

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")

        testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.mockk:mockk:1.13.5")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "17"
            }
        }
        test {
            useJUnitPlatform()
        }
    }
}

configure(
    subprojects.filter {
        it.path in listOf(
            ":adapter-in:support:auth",
            ":adapter-in:support:oas",
            ":domain",
        )
    },
) {
    configurations.register("testArchive") {
        extendsFrom(configurations.testImplementation.get())
    }
    tasks.register<Jar>(name = "testJar") {
        from(project.sourceSets.test.get().output)
        archiveClassifier.set("test")
    }
    artifacts {
        add("testArchive", tasks.getByName("testJar"))
    }
}
