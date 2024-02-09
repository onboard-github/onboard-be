plugins {
    id("java")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    val springVersion by properties

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
}

tasks.test {
    useJUnitPlatform()
}
