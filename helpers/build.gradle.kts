
plugins {
    id("java-library")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")
apply(from = "$gradleScripts/native.gradle")

description = "Hexagon helpers."

dependencies {
    val scriptMockkVersion = findProperty("mockkVersion")

    "api"("com.hexagonkt:core:$version")

    "testImplementation"("org.jetbrains.kotlin:kotlin-reflect")
    "testImplementation"("io.mockk:mockk:$scriptMockkVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }
}
