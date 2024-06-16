
rootProject.name = "hexagon_extra"

include(
    // Utility modules
    "args",
    "converters",
    "scheduler",
    "models",
    "injection",
    "jul",
    "terminal",

    // Ports
    "messaging",
    "store",

    // Adapters
    "messaging_rabbitmq",
    "store_mongodb",

    // Tools
    "dokka_json",
    "processor",

    // testing
    "application_test",
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Build
            version("kotlin", "2.0.0")
            version("dokka", "1.9.20")
            version("licenseReport", "2.8")
            version("binValidator", "0.15.0-Beta.2")
            version("nativeTools", "0.10.2")
            version("detekt", "1.23.6")
            version("gradleWrapper", "8.8")
            version("mermaidDokka", "0.6.0")

            // Testing
            version("testcontainers", "1.19.8")
            version("dockerJava", "3.3.6")
            version("commonsCompress", "1.26.2")

            // messaging_rabbitmq
            version("rabbit", "5.21.0")
            version("metricsJmx", "4.2.25")

            // scheduler
            version("cronutils", "9.2.1")

            // store_mongodb
            version("mongodb", "5.1.1")

            // models
            version("jakartaMail", "2.0.1")
        }
    }
}
