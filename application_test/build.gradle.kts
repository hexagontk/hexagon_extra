
plugins {
    id("org.graalvm.buildtools.native") version("0.9.19")
}

val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/detekt.gradle")
apply(from = "$gradleScripts/application.gradle")
apply(from = "$gradleScripts/native.gradle")

description = "."

extensions.configure<JavaApplication> {
    mainClass.set("com.hexagonkt.application.test.ApplicationKt")
}

dependencies {
    "api"(project(":terminal"))
}
