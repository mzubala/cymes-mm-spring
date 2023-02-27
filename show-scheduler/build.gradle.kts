plugins {
    `app-module`
}

dependencies {
    implementation(project(":cinemas"))
    implementation(project(":movies"))
    implementation(project(":commons:shared-kernel"))
    implementation(project(":commons:commons-application"))
}