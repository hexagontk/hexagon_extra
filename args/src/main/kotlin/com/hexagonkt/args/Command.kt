package com.hexagonkt.args

import com.hexagonkt.helpers.requireNotBlank

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
    val flags: LinkedHashSet<Flag> =
        LinkedHashSet(properties.filterIsInstance<Flag>())

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

    val flagsMap: Map<String, Flag> =
        propertiesMap
            .filterValues { it is Flag }
            .mapValues { it.value as Flag }

    val optionsMap: Map<String, Option<*>> =
        propertiesMap
            .filterValues { it is Option<*> }
            .mapValues { it.value as Option<*> }

    val parametersMap: Map<String, Parameter<*>> =
        propertiesMap
            .filterValues { it is Parameter<*> }
            .mapValues { it.value as Parameter<*> }

    val subcommandsMap: Map<String, Command> =
        nestedSubcommands().associateBy { it.name }

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

    fun findCommand(args: Array<String>): Command {
        val line = args.joinToString(" ")
        return subcommandsMap
            .mapKeys { it.key.removePrefix("$name ") }
            .entries
            .sortedByDescending { it.key.count { c -> c == ' ' } }
            .find { line.contains(it.key) }
            ?.let { (k, v) -> v.copy(name = k) }
            ?: this
    }

    fun parse(args: List<String>): Command {
        val propertiesIterator = args.iterator()
        var parsedProperties = emptyList<Property<*>>()
        var parsedParameters = parameters

        propertiesIterator.forEach {
            parsedProperties += when {
                it.startsWith("--") -> parseOption(it.removePrefix("--"), propertiesIterator)
                it.startsWith('-') -> parseShortOptions(it.removePrefix("-"), propertiesIterator)
                else -> parseArgument(it, parsedParameters)
            }
        }

        return copy(properties = LinkedHashSet(parsedProperties))
    }

    private fun parseArgument(it: String, p: LinkedHashSet<Parameter<*>>): Collection<Property<*>> {
        return listOf(Parameter(String::class, "a", value = it))
    }

    private fun parseShortOptions(it: String, propertiesIterator: Iterator<String>): Collection<Property<*>> {
        val p =
            if (it.contains('=')) it.split('=', limit = 2).let { (f, s) -> f to s }
            else it to null

        return p.first.map {
            when (val o = propertiesMap[it.toString()] ?: error("")) {
                is Option<*> -> {
                    val param = p.second ?: propertiesIterator.next()
                    if (param.startsWith('-'))
                        error("")
                    o.addValue(param)
                }
                is Flag -> o.addValue("true")
                else -> error("")
            }
        }
    }

    private fun parseOption(it: String, propertiesIterator: Iterator<String>): Collection<Property<*>> {
        val p =
            if (it.contains('=')) it.split('=', limit = 2).let { (f, s) -> f to s }
            else it to null

        val o1 = when (val o = propertiesMap[p.first] ?: error("")) {
            is Option<*> -> {
                val param = p.second ?: propertiesIterator.next()
                if (param.startsWith('-'))
                    error("")
                o.addValue(param)
            }
            is Flag -> o.addValue("true")
            else -> error("")
        }

        return listOf(o1)
    }

    private fun nestedSubcommands(): LinkedHashSet<Command> =
        subcommands
            .map { it.copy(name = name + " " + it.name) }
            .let { c -> c + c.flatMap { it.nestedSubcommands() } }
            .let(::LinkedHashSet)
}
