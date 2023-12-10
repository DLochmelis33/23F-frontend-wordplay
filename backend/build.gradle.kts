plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    id("io.ktor.plugin") version "2.3.5"
    application
}

group = "me.dl33"
version = "0.0"

application {
    mainClass = "me.dl33.wordplay.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-cio")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-server-websockets")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.+")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}