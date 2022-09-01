
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

dependencies {
    val kotlinxHtmlVersion = properties["kotlinxHtmlVersion"]

    "api"("com.hexagonkt:http_server:$version")
    "api"("com.hexagonkt:templates:$version")
    "api"("com.hexagonkt:serialization:$version")
    "api"("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")

    "testImplementation"("com.hexagonkt:http_client_jetty:$version")
    "testImplementation"("com.hexagonkt:http_server_jetty:$version")
    "testImplementation"("com.hexagonkt:templates_pebble:$version")
    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
}
