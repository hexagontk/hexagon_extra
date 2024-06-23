
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
    val mongodbVersion = libs.versions.mongodb.get()
    val testcontainersVersion = libs.versions.testcontainers.get()
    val dockerJavaVersion = libs.versions.dockerJava.get()
    val commonsCompressVersion = libs.versions.commonsCompress.get()

    "api"(project(":store"))
    "api"("org.mongodb:mongodb-driver-sync:$mongodbVersion")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
    "testImplementation"("com.github.docker-java:docker-java-api:$dockerJavaVersion")
    "testImplementation"("com.github.docker-java:docker-java-transport-zerodep:$dockerJavaVersion")
    "testImplementation"("org.apache.commons:commons-compress:$commonsCompressVersion")
    "testImplementation"("org.testcontainers:mongodb:$testcontainersVersion") {
        exclude(module = "commons-compress")
    }
}
