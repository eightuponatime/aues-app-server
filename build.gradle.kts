val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt") // Укажите главный класс вашего приложения Ktor
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

val selenium_version = "4.11.0"
val exposed_version = "0.41.1"
val h2_version = "2.1.214"

dependencies {

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation ("mysql:mysql-connector-java:8.0.28")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")

    implementation(kotlin("stdlib"))
    implementation("org.seleniumhq.selenium:selenium-java:$selenium_version")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")


    implementation ("io.ktor:ktor-websockets")
    implementation("io.ktor:ktor-server-websockets")
    implementation("io.ktor:ktor-network") //for sockets
    implementation("io.ktor:ktor-server-core")

    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-logging")

    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
