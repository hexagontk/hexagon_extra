
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")
apply(from = "$gradleScripts/native.gradle")

description = "Data classes processor."

dependencies {
    "api"("org.jetbrains.kotlin:kotlin-reflect")

    "api"(project(":models"))
    "api"("com.hexagonkt:http:$version")
    "api"("com.hexagonkt:serialization_jackson_json:$version")
    "api"("com.hexagonkt:serialization_jackson_yaml:$version")
}
