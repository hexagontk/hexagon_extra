
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

    // Testing
    "application_test",
)
