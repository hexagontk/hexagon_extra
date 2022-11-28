
rootProject.name = "hexagon_extra"

include(
    // Internal modules
    "args",
    "converters",
    "scheduler",
    "web",
    "models",
    "rest",
    "injection",

    // Ports
    "messaging",
    "store",

    // Adapters
    "messaging_rabbitmq",
    "store_mongodb",

    // Tools
    "dokka_json",
    "data_processor",
)
