plugins {
    kotlin("jvm") version "1.9.0"
}

group = "me.dl33"
version = "0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}