
val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/gradle/kotlin.gradle")
apply(from = "$gradleScripts/gradle/publish.gradle")
apply(from = "$gradleScripts/gradle/dokka.gradle")

extra["basePackage"] = "com.hexagonkt.messaging"

dependencies {
    "api"("com.hexagonkt:hexagon_core:$version")
}
