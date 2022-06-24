
rootProject.name = "hexagon_extra"

include(
    // Internal modules
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
)
