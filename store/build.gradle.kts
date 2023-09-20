
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")
apply(from = "$gradleScripts/native.gradle")

dependencies {
    "api"(project(":converters"))
    "api"("com.hexagonkt:core:$version")
    "api"("com.hexagonkt:serialization:$version")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
}
