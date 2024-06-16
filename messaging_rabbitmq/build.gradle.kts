
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")

dependencies {
    val rabbitVersion = libs.versions.rabbit.get()
    val testcontainersVersion = libs.versions.testcontainers.get()
    val metricsJmxVersion = libs.versions.metricsJmx.get()
    val commonsCompressVersion = libs.versions.commonsCompress.get()

    "api"("com.hexagonkt:http:$version")
    "api"("com.hexagonkt:serialization:$version")
    "api"(project(":converters"))
    "api"(project(":messaging"))
    "api"("com.rabbitmq:amqp-client:$rabbitVersion")
    "api"("io.dropwizard.metrics:metrics-jmx:$metricsJmxVersion")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
    "testImplementation"("org.apache.commons:commons-compress:$commonsCompressVersion")
    "testImplementation"("org.testcontainers:rabbitmq:$testcontainersVersion") {
        exclude(module = "commons-compress")
    }
}
