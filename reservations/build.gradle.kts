plugins {
    `app-module`
    id("com.github.bjornvester.wsdl2java") version "1.2"
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("javax.xml.bind:jaxb-api:2.3.0")
        classpath("org.glassfish.jaxb:jaxb-runtime:2.3.0")
        classpath("javax.annotation:javax.annotation-api:1.3.2")
        classpath("javax.activation:activation:1.1.1")
    }
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