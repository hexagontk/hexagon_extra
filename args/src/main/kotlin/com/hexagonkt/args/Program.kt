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
        val programCommand = command.findCommand(args)
        val subcommands = programCommand.name.split(" ")
        val properties = subcommands.fold(args.toList()) { a, b -> a - b }

        // TODO Handle exceptions and --help, --version
        return programCommand.parse(properties)
    }
}
