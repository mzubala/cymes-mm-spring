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
    implementation(project(":users"))
    implementation(project(":reservations"))
    implementation(project(":commons:commons-rest"))
    implementation(project(":commons:commons-configuration"))
    implementation(project(":commons:shared-kernel"))

    testImplementation(project(":commons:commons-test"))
}