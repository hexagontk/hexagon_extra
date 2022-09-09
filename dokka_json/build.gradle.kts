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
    val dokkaVersion = properties["dokkaVersion"]

    "api"("com.hexagonkt:core:$version")
    "api"("com.hexagonkt:serialization_jackson_json:$version")
    "api"("org.jetbrains.dokka:dokka-base:$dokkaVersion")
    "compileOnly"("org.jetbrains.dokka:dokka-core:$dokkaVersion")
}

tasks.register<DokkaTask>("dokkaJson") {
    dependencies {
        plugins(project(":dokka_json"))
    }
}
