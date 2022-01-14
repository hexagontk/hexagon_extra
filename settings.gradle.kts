
rootProject.name = "hexagon_extra"

include(
    // Internal modules
    "scheduler",
    "web",

    // Ports
    "messaging",
    "store",

    // Adapters
    "messaging_rabbitmq",
    "store_mongodb",
)
