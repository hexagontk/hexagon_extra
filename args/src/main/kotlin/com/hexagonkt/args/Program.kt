package com.hexagonkt.args

import com.hexagonkt.args.Property.Companion.HELP
import com.hexagonkt.args.Property.Companion.VERSION
import com.hexagonkt.args.formatter.CommandFormatter
import com.hexagonkt.args.formatter.ProgramFormatter
import com.hexagonkt.helpers.CodedException
import com.hexagonkt.helpers.requireNotBlank
import java.io.BufferedReader
import kotlin.system.exitProcess

data class Program(
    val version: String? = null,
    val command: Command,
    val formatter: Formatter<Program> = ProgramFormatter(),
    val commandFormatter: Formatter<Command> = CommandFormatter(),
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
            if (it.ready()) it.readText()
            else null
        }

    fun parse(args: Array<String>): Command =
        try {
            process(args)
        }
        catch (e: CodedException) {
            System.err.println(e.message)
            exitProcess(e.code)
        }

    internal fun process(args: Array<String>): Command {
        val programCommand = command.findCommand(args)
        val subcommands = programCommand.name.split(" ")
        val properties = subcommands.fold(args.toList()) { a, b -> a - b }

        val parsedCommand = try {
            programCommand.parse(properties)
        }
        catch (e: Exception) {
            val helpMessage = "Use the --help option (-h) to get more information."
            val message = "${e.message}\n\n${usage(programCommand)}\n\n$helpMessage"
            throw CodedException(400, message, e)
        }

        if (parsedCommand.flags.contains(VERSION.addValue("true")))
            throw CodedException(0, formatter.summary(this))

        if (parsedCommand.flags.contains(HELP.addValue("true"))) {
            val message = listOf(
                formatter.summary(this),
                usage(programCommand),
                commandFormatter.detail(programCommand)
            ).joinToString("\n\n")

            throw CodedException(0, message)
        }

        return parsedCommand
    }

    private fun usage(programCommand: Command): String =
        "USAGE:\n  " + commandFormatter.definition(programCommand)
}
