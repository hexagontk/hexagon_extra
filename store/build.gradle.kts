
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")

dependencies {
    "api"(project(":converters"))
    "api"("com.hexagonkt:helpers:$version")
    "api"("com.hexagonkt:serialization:$version")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
}
