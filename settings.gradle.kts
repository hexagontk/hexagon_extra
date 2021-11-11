
rootProject.name = "hexagon_extra"

include(
    // Internal modules
    "scheduler",

    // Ports
    "messaging",
    "store",

    // Adapters
    "messaging_rabbitmq",
    "store_mongodb",
)
