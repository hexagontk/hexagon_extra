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

    fun parse(args: Array<out String>): Command =
        parse(args.toList())

    fun parse(args: Iterable<String>): Command =
        try {
            process(args)
        }
        catch (e: CodedException) {
            System.err.println(e.message)
            exitProcess(e.code)
        }

    internal fun process(args: Iterable<String>): Command {
        val programCommand = command.findCommand(args)
        val subcommands = programCommand.name.split(" ")
        val properties = subcommands.fold(args.toList()) { a, b -> a - b }

        if (programCommand.contains(VERSION, properties))
            throw CodedException(0, formatter.summary(this))

        if (programCommand.contains(HELP, properties)) {
            val message = listOf(
                definition(programCommand),
                usage(programCommand),
                commandFormatter.detail(programCommand)
            ).joinToString("\n\n")

            throw CodedException(0, message)
        }

        val parsedCommand = try {
            programCommand.parse(properties)
        }
        catch (e: Exception) {
            val helpMessage = "Use the --help option (-h) to get more information"
            val message = "${e.message}\n\n${usage(programCommand)}\n\n$helpMessage"
            throw CodedException(400, message, e)
        }

        return parsedCommand
    }

    private fun definition(programCommand: Command): String =
        if (programCommand == command)
            formatter.summary(this)
        else
            commandFormatter.summary(programCommand)

    private fun usage(programCommand: Command): String =
        if (programCommand == command)
            formatter.definition(this) + commandFormatter.definition(programCommand)
        else
            formatter.definition(this) + command.name + " " + commandFormatter.definition(programCommand)
}
