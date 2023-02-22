package com.hexagonkt.args

import com.hexagonkt.args.formatter.ProgramFormatter
import com.hexagonkt.helpers.out
import com.hexagonkt.helpers.requireNotBlank
import java.io.BufferedReader
import kotlin.system.exitProcess

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
        properties: Set<Property<*>> = emptySet(),
    ) : this(version, Command(name, title, description, properties))

    constructor(
        name: String,
        version: String? = null,
        title: String? = null,
        description: String? = null,
        properties: Set<Property<*>> = emptySet(),
        commands: Set<Command>,
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

        return try {
            val parsedCommand = programCommand.parse(properties)

            if (parsedCommand.flags.contains(VERSION))
                showVersion()

            if (parsedCommand.flags.contains(HELP))
                showHelp()

            // TODO Check mandatory fields
            parsedCommand
        }
        catch (e: Exception) {
            showErrors(e)
        }
    }

    private fun showVersion() {
        exitProcess(0)
    }

    private fun showHelp() {
        exitProcess(0)
    }

    private fun showErrors(e: Exception): Nothing {
        // TODO Handle exceptions
        exitProcess(400)
    }
}
