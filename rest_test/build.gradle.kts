
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")

description = "Test cases for HTTP client and server adapters."

dependencies {
    val swaggerParserVersion = properties["swaggerParserVersion"]
    val vertxVersion = properties["vertxVersion"]

    "api"(project(":rest"))
    "api"("com.hexagonkt:http_server:$version")
    "api"("com.hexagonkt:http_client:$version")
    "api"("com.hexagonkt:serialization:$version")
    "api"("io.swagger.parser.v3:swagger-parser:$swaggerParserVersion")
    "api"("io.vertx:vertx-json-schema:$vertxVersion")

    "testImplementation"("com.hexagonkt:http_client_jetty:$version")
    "testImplementation"("com.hexagonkt:http_server_jetty:$version")
    "testImplementation"("com.hexagonkt:http_server_netty:$version")
    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
    "testImplementation"("com.hexagonkt:serialization_jackson_yaml:$version")
}
