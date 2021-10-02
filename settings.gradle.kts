
rootProject.name = "hexagon_extra"

include(
    // Internal modules
    "hexagon_scheduler",

    // Ports
    "port_messaging",
    "port_store",

    // Adapters
    "messaging_rabbitmq",
    "store_mongodb",
)
