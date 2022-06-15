
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

extra["basePackage"] = "com.hexagonkt.rest"

dependencies {
    "api"("com.hexagonkt:http_server:$version")
    "api"("com.hexagonkt:serialization:$version")

    "testImplementation"("com.hexagonkt:http_client_jetty:$version")
    "testImplementation"("com.hexagonkt:http_server_jetty:$version")
    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
}
