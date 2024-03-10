
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/native.gradle")
apply(from = "$gradleScripts/detekt.gradle")

description = "Java Util Logging utilities."

dependencies {
    "api"("com.hexagonkt:core:$version")
}
