
val gradleScripts = properties["gradleScripts"]

apply(from = "$gradleScripts/kotlin.gradle")
apply(from = "$gradleScripts/publish.gradle")
apply(from = "$gradleScripts/dokka.gradle")

description = "Hexagon support for repeated tasks execution based on Cron expressions."

extra["basePackage"] = "com.hexagonkt.scheduler"

dependencies {
    val cronutilsVersion = properties["cronutilsVersion"]

    "api"("com.hexagonkt:core:$version")
    "api"("com.cronutils:cron-utils:$cronutilsVersion")
}
