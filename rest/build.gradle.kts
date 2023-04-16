
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
    "api"("com.hexagonkt:http:$version")
    "api"("com.hexagonkt:serialization:$version")

    "testImplementation"("com.hexagonkt:http_server:$version")
    "testImplementation"("com.hexagonkt:http_client:$version")
    "testImplementation"("com.hexagonkt:http_client_jetty:$version")
    "testImplementation"("com.hexagonkt:http_server_jetty:$version")
    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
}
