import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "pl.com.bottega"
version = "0.0.1-SNAPSHOT"

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-test")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.1.5.Final")
    implementation("org.postgresql:postgresql")
    implementation("com.google.guava:guava:31.1-jre")
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}