
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

description = "."

extra["basePackage"] = "com.hexagonkt.injection"

dependencies {
    "api"("com.hexagonkt:core:$version")
}
