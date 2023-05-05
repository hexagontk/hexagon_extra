package com.hexagonkt.args

import com.hexagonkt.args.Property.Companion.HELP
import com.hexagonkt.args.Property.Companion.VERSION
import com.hexagonkt.args.formatter.DefaultFormatter
import com.hexagonkt.helpers.CodedException
import com.hexagonkt.helpers.requireNotBlank
import java.io.BufferedReader

data class Program(
    val version: String? = null,
    val command: Command,
    val formatter: Formatter = DefaultFormatter(),
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
        process(args)

    internal fun process(args: Iterable<String>): Command {
        val programCommand = command.findCommand(args)
        val subcommands = programCommand.name.split(" ")
        val properties = subcommands.fold(args.toList()) { a, b -> a - b }

        if (programCommand.contains(VERSION, properties))
            throw CodedException(0, formatter.summary(this, programCommand))

        if (programCommand.contains(HELP, properties)) {
            val message = formatter.help(this, programCommand)
            throw CodedException(0, message)
        }

        val parsedCommand = try {
            programCommand.parse(properties)
        }
        catch (e: Exception) {
            val message = formatter.error(this, programCommand, e)
            throw CodedException(400, message, e)
        }

        return parsedCommand
    }
}
