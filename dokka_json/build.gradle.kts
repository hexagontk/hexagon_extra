import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")

description = "."

dependencies {
    val dokkaVersion = libs.versions.dokka.get()

    "api"("com.hexagonkt:core:$version")
    "api"("com.fasterxml.jackson.core:jackson-databind:2.15.3") // Dokka requires this fixed version
    "api"("org.jetbrains.dokka:dokka-base:$dokkaVersion")
    "compileOnly"("org.jetbrains.dokka:dokka-core:$dokkaVersion")

    "testImplementation"("org.jetbrains.dokka:dokka-test-api:$dokkaVersion") {
        exclude("org.jetbrains.kotlin")
    }
    "testImplementation"("org.jetbrains.dokka:dokka-base-test-utils:$dokkaVersion") {
        exclude("org.jetbrains.kotlin")
    }
    "testImplementation"("org.jetbrains:markdown:0.3.1") { // Dokka requires this fixed version
        exclude("org.jetbrains.kotlin")
    }
}

tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
    dependencies {
        plugins(project(":dokka_json"))
    }
}
