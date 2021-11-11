
val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/gradle/kotlin.gradle")
apply(from = "$gradleScripts/gradle/publish.gradle")
apply(from = "$gradleScripts/gradle/dokka.gradle")

extra["basePackage"] = "com.hexagonkt.store.mongodb"

dependencies {
    val mongodbVersion = properties["mongodbVersion"]
    val testcontainersVersion = properties["testcontainersVersion"]

    "api"(project(":store"))
    "api"("org.mongodb:mongodb-driver-sync:$mongodbVersion")

    "testImplementation"("com.hexagonkt:serialization_json:$version")
    "testImplementation"("org.testcontainers:mongodb:$testcontainersVersion")
}
