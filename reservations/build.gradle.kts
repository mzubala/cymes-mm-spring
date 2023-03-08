plugins {
    `app-module`
}

dependencies {
    implementation(project(":commons:shared-kernel"))
    implementation(project(":show-scheduler"))

    testImplementation(project(":commons:commons-test"))
}