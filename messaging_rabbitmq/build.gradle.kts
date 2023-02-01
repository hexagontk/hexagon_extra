
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

dependencies {
    val rabbitVersion = properties["rabbitVersion"]
    val testcontainersVersion = properties["testcontainersVersion"]
    val metricsJmxVersion = properties["metricsJmxVersion"]

    "api"("com.hexagonkt:http:$version")
    "api"("com.hexagonkt:serialization:$version")
    "api"(project(":converters"))
    "api"(project(":messaging"))
    "api"("com.rabbitmq:amqp-client:$rabbitVersion")
    "api"("io.dropwizard.metrics:metrics-jmx:$metricsJmxVersion")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
    "testImplementation"("org.testcontainers:rabbitmq:$testcontainersVersion")
}
