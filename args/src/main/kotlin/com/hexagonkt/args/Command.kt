package com.hexagonkt.args

import com.hexagonkt.core.requireNotBlank

// A program can have multiple commands with their own set of options and positional parameters
// name: all nested "subcommands" separated by spaces. I.e.: "config get"
data class Command(
    override val name: String,
    override val title: String? = null,
    override val description: String? = null,
    override val properties: LinkedHashSet<Property<*>> = linkedSetOf(),
) : Operation {

    override val propertiesMap: Map<String, Property<*>> by lazy {
        properties.associateBy { p ->
            when (p) {
                is Parameter -> p.name ?: ""
                is Option -> p.shortName?.toString() ?: ""
            }
        }
    }

    init {
        requireNotBlank(Command::name)
        requireNotBlank(Command::description)
    }
}
