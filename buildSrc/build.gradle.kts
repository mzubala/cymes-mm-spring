plugins {
    `kotlin-dsl`
    id("org.springframework.boot") version "3.0.0" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
    id("io.freefair.lombok") version "6.6" apply false
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.0.0")
    implementation("io.freefair.gradle:lombok-plugin:6.6")
}


repositories {
    mavenCentral()
}