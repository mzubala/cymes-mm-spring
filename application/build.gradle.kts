import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `app-module`
}

tasks.named<BootJar>("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":cinemas"))
    implementation(project(":movies"))
    implementation(project(":show-scheduler"))
    implementation(project(":commons:commons-rest"))

    testImplementation(project(":commons:commons-test"))
}