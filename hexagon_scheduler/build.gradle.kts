
val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/gradle/kotlin.gradle")
apply(from = "$gradleScripts/gradle/publish.gradle")
apply(from = "$gradleScripts/gradle/dokka.gradle")

description = "Hexagon support for repeated tasks execution based on Cron expressions."

extra["basePackage"] = "com.hexagonkt.scheduler"

dependencies {
    val cronutilsVersion = properties["cronutilsVersion"]

    "api"("com.hexagonkt:hexagon_core:$version")
    "api"("com.cronutils:cron-utils:$cronutilsVersion")
}
