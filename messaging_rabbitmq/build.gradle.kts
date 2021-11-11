
val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/gradle/kotlin.gradle")
apply(from = "$gradleScripts/gradle/publish.gradle")
apply(from = "$gradleScripts/gradle/dokka.gradle")

extra["basePackage"] = "com.hexagonkt.messaging.rabbitmq"

dependencies {
    val rabbitVersion = properties["rabbitVersion"]
    val testcontainersVersion = properties["testcontainersVersion"]
    val metricsJmxVersion = properties["metricsJmxVersion"]

    val qpidVersion = properties["qpidVersion"]
    val logbackVersion = properties["logbackVersion"]

    "api"("com.hexagonkt:http:$version")
    "api"(project(":messaging"))
    "api"("com.rabbitmq:amqp-client:$rabbitVersion") {
        exclude(module = "slf4j-api")
    }
    "api"("io.dropwizard.metrics:metrics-jmx:$metricsJmxVersion")

    "testImplementation"("org.apache.qpid:qpid-broker:$qpidVersion") {
        exclude(module = "logback-classic")
        exclude(module = "jackson-databind")
        exclude(module = "jackson-core")
        exclude(module = "slf4j-api")
        exclude(module = "qpid-broker-plugins-derby-store")
        exclude(module = "qpid-broker-plugins-jdbc-provider-bone")
        exclude(module = "qpid-broker-plugins-jdbc-store")
        exclude(module = "qpid-broker-plugins-management-http")
        exclude(module = "qpid-broker-plugins-websocket")
    }

    "testImplementation"("com.hexagonkt:serialization_json:$version")
    "testImplementation"("ch.qos.logback:logback-classic:$logbackVersion") {
        exclude(group = "org.slf4j")
    }
    "testImplementation"("org.testcontainers:rabbitmq:$testcontainersVersion")
}
