
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

    val qpidVersion = properties["qpidVersion"]
    val logbackVersion = properties["logbackVersion"]

    "api"("com.hexagonkt:http:$version")
    "api"("com.hexagonkt:serialization:$version")
    "api"(project(":messaging"))
    "api"("com.rabbitmq:amqp-client:$rabbitVersion")
    "api"("io.dropwizard.metrics:metrics-jmx:$metricsJmxVersion")

    "testImplementation"("org.apache.qpid:qpid-broker:$qpidVersion") {
        exclude(module = "jackson-databind")
        exclude(module = "jackson-core")
        exclude(module = "slf4j-api")
        exclude(module = "qpid-broker-plugins-derby-store")
        exclude(module = "qpid-broker-plugins-jdbc-provider-bone")
        exclude(module = "qpid-broker-plugins-jdbc-store")
        exclude(module = "qpid-broker-plugins-management-http")
        exclude(module = "qpid-broker-plugins-websocket")
    }

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
    "testImplementation"("org.testcontainers:rabbitmq:$testcontainersVersion")
}
