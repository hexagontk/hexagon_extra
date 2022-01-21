
val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

description = "Commonly used data models (like addresses)."

extra["basePackage"] = "com.hexagonkt.models"

dependencies {
    val javamailVersion = properties["javamailVersion"]

    "api"("com.hexagonkt:core:$version")
    "api"("com.sun.mail:javax.mail:$javamailVersion")
}
