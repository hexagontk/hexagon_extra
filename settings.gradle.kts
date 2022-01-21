
rootProject.name = "hexagon_extra"

include(
    // Internal modules
    "scheduler",
    "web",
    "models",

    // Ports
    "messaging",
    "store",

    // Adapters
    "messaging_rabbitmq",
    "store_mongodb",
)
