
rootProject.name = "hexagon_extra"

include(
    // Utility modules
    "args",
    "converters",
    "scheduler",
    "web",
    "models",
    "rest",
    "rest_test",
    "injection",
    "terminal",

    // Ports
    "store",

    // Adapters
    "store_mongodb",

    // Tools
    "dokka_json",

    // Testing
    "application_test",
)
