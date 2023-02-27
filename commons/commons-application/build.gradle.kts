plugins {
    `app-module`
}

dependencies {
    implementation(project(":commons:shared-kernel"))
    testImplementation(project(":commons:commons-test"))
}