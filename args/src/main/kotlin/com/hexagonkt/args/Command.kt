package com.hexagonkt.args

import com.hexagonkt.core.requireNotBlank

/**
 * A program can have multiple commands with their own set of options and positional parameters.
 */
data class Command(
    val name: String,
    val title: String? = null,
    val description: String? = null,
    val properties: LinkedHashSet<Property<*>> = linkedSetOf(),
    val subcommands: LinkedHashSet<Command> = LinkedHashSet(),
) {
    val options: LinkedHashSet<Option<*>> =
        LinkedHashSet(properties.filterIsInstance<Option<*>>())

    val parameters: LinkedHashSet<Parameter<*>> =
        LinkedHashSet(properties.filterIsInstance<Parameter<*>>())

    val propertiesMap: Map<String, Property<*>> =
        properties
            .flatMap { p ->
                p.names.map { it to p }
            }
            .toMap()

    val defaultsMap: Map<String, Property<*>> =
        propertiesMap.filterValues { it.values.isNotEmpty() }

    val optionsMap: Map<String, Option<*>> =
        propertiesMap
            .filterValues { it is Option<*> }
            .mapValues { it.value as Option<*> }

    val parametersMap: Map<String, Parameter<*>> =
        propertiesMap
            .filterValues { it is Parameter<*> }
            .mapValues { it.value as Parameter<*> }

    init {
        requireNotBlank(Command::name)
        requireNotBlank(Command::title)
        requireNotBlank(Command::description)

        if (parametersMap.isNotEmpty()) {
            val parameters = parametersMap.values.reversed().drop(1)
            require(parameters.all { !it.multiple }) {
                "Only the last positional parameter can be multiple"
            }
        }
    }

    fun nestedSubcommands(): LinkedHashSet<Command> =
        subcommands
            .map { it.copy(name = name + " " + it.name) }
            .let { c -> c + c.flatMap { it.nestedSubcommands() } }
            .let(::LinkedHashSet)

    fun subcommandsMap(): Map<String, Command> =
        nestedSubcommands().associateBy { it.name }
}
