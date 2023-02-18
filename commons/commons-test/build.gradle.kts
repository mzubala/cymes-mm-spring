plugins {
    `app-module`
}

dependencies {
    api("com.github.javafaker:javafaker:1.0.2")
    api("org.junit.jupiter:junit-jupiter:5.8.2")
    api("org.assertj:assertj-core:3.6.1")
    api("org.springframework.boot:spring-boot-starter-test")
    implementation("org.testcontainers:postgresql:1.17.6")
}