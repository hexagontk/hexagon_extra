package com.hexagonkt.args

import com.hexagonkt.args.formatter.ProgramFormatter
import com.hexagonkt.core.out
import com.hexagonkt.core.requireNotBlank
import java.io.BufferedReader

data class Program(
    val version: String? = null,
    val command: Command,
    val formatter: Formatter<Program> = ProgramFormatter(),
) {
//    private val allOptions: Map<String, Option<*>> =
//        command.nestedSubcommands().map { it.optionsMap }.reduce { a, b -> a + b }

    constructor(
        name: String,
        version: String? = null,
        title: String? = null,
        description: String? = null,
        properties: LinkedHashSet<Property<*>> = linkedSetOf(),
    ) : this(version, Command(name, title, description, properties))

    constructor(
        name: String,
        version: String? = null,
        title: String? = null,
        description: String? = null,
        properties: LinkedHashSet<Property<*>> = linkedSetOf(),
        commands: LinkedHashSet<Command>,
    ) : this(version, Command(name, title, description, properties, commands))

    init {
        requireNotBlank(Program::version)
    }

    fun input(): String? =
        BufferedReader(System.`in`.reader()).use {
            if (it.ready().out()) it.readText()
            else null
        }

    fun parse(args: Array<String>): Command {
        val arguments = args.toList()

        // Find command
        val line = arguments.joinToString(" ")
        val subcommandsMap = command.subcommandsMap()
        val commandKey = subcommandsMap.keys.find { line.contains(it) } ?: ""
        val programCommand = subcommandsMap[commandKey] ?: command

        // Remove command from args
        val properties = commandKey.split(" ").fold(arguments) { a, b -> a - b }
        val propertiesIterator = properties.iterator()

        // Process properties
        for (arg in propertiesIterator) {
            //
            when {
                arg.startsWith("--") -> {

                }
                arg.startsWith("-") -> {

                }
            }
        }

        return programCommand
    }
}
