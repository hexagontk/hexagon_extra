
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

extra["basePackage"] = "com.hexagonkt.messaging"

dependencies {
    "api"("com.hexagonkt:core:$version")
}
