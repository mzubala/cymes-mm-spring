plugins {
    `app-module`
}

dependencies {
    implementation(project(":commons:shared-kernel"))
    implementation(project(":commons:commons-events"))
    implementation(project(":reservations"))
}