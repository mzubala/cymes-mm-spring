plugins {
    `app-module`
}

dependencies {
    implementation(project(":commons:shared-kernel"))
    implementation(project(":show-scheduler"))
    implementation(project(":users"))
    implementation(project(":cinemas"))
    implementation(project(":commons:commons-application"))

    testImplementation(project(":commons:commons-test"))
}