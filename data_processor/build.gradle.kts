import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

description = "."

dependencies {
    "api"("com.hexagonkt:core:$version")
}

tasks.register<DokkaTask>("dokkaJson") {
    dependencies {
        plugins(project(":dokka_json"))
    }
}
