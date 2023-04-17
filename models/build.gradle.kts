import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")
apply(from = "$gradleScripts/native.gradle")

description = "Commonly used data models (like addresses)."

dependencies {
    val jakartaMailVersion = properties["jakartaMailVersion"]

    "api"(project(":helpers"))
    "api"("com.sun.mail:jakarta.mail:$jakartaMailVersion")
}

tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
    dependencies {
        plugins(project(":dokka_json"))
    }
}
