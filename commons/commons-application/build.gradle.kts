plugins {
    `app-module`
}

dependencies {
    implementation(project(":commons:shared-kernel"))
    testImplementation(project(":commons:commons-test"))
    testImplementation(project(":commons:commons-configuration"))
}