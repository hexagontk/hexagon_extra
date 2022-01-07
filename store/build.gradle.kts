
val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/gradle/kotlin.gradle")
apply(from = "$gradleScripts/gradle/publish.gradle")
apply(from = "$gradleScripts/gradle/dokka.gradle")

extra["basePackage"] = "com.hexagonkt.store"

dependencies {
    val kotlinVersion = properties["kotlinVersion"]

    "api"("com.hexagonkt:core:$version")
    "api"("com.hexagonkt:serialization:$version")
    "api"("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    "testImplementation"("com.hexagonkt:serialization_jackson_json:$version")
}
