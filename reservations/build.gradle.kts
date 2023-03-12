plugins {
    `app-module`
}

dependencies {
    implementation(project(":commons:shared-kernel"))
    implementation(project(":show-scheduler"))
    implementation(project(":users"))
    implementation(project(":cinemas"))
    implementation(project(":commons:commons-application"))
    implementation(project(":commons:commons-rest"))

    testImplementation(project(":commons:commons-test"))
}