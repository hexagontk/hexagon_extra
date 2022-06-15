
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

extra["basePackage"] = "com.hexagonkt.store.mongodb"

dependencies {
    val mongodbVersion = properties["mongodbVersion"]
    val testcontainersVersion = properties["testcontainersVersion"]

    "api"(project(":store"))
    "api"("org.mongodb:mongodb-driver-sync:$mongodbVersion")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
    "testImplementation"("org.testcontainers:mongodb:$testcontainersVersion")
}
