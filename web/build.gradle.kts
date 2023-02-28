
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/detekt.gradle")

dependencies {
    "api"("com.hexagonkt:http_server:$version")
    "api"("com.hexagonkt:templates:$version")

    "testImplementation"(project(":converters"))
    "testImplementation"("com.hexagonkt:logging_jul:$version")
    "testImplementation"("com.hexagonkt:http_client_jetty:$version")
    "testImplementation"("com.hexagonkt:http_server_jetty:$version")
    "testImplementation"("com.hexagonkt:templates_pebble:$version")
    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
}
