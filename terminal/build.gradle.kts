
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

description = "Hexagon core utilities. Includes serialization and logging helpers."

dependencies {
    "api"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
