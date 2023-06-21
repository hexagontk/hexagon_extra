
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")

dependencies {
    val mongodbVersion = properties["mongodbVersion"]
    val testcontainersVersion = properties["testcontainersVersion"]
    val dockerJavaVersion = properties["dockerJavaVersion"]

    "api"(project(":store"))
    "api"("org.mongodb:mongodb-driver-sync:$mongodbVersion")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
    "testImplementation"("org.testcontainers:mongodb:$testcontainersVersion")
    "testImplementation"("com.github.docker-java:docker-java-api:$dockerJavaVersion")
    "testImplementation"("com.github.docker-java:docker-java-transport-zerodep:$dockerJavaVersion")
}
