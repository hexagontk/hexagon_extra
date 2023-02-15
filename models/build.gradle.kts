import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

description = "Commonly used data models (like addresses)."

dependencies {
    val javamailVersion = properties["javamailVersion"]

    "api"("com.hexagonkt:helpers:$version")
    "api"("com.sun.mail:javax.mail:$javamailVersion")
}

tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
    dependencies {
        plugins(project(":dokka_json"))
    }
}
