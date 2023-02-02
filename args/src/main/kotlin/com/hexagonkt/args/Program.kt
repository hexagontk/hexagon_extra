package com.hexagonkt.args

import com.hexagonkt.core.out
import com.hexagonkt.core.requireNotBlank
import java.io.BufferedReader

data class Program(
    val version: String?,
    val programCommand: Command,
    val commands: LinkedHashSet<Command>,
) : Operation by programCommand {

    val commandsMap: Map<String, Command> by lazy {
        commands
            .associateBy { it.name }
            .mapValues { (_, v) -> v.copy(properties = LinkedHashSet(properties + v.properties)) }
    }

    constructor(
        name: String,
        version: String? = null,
        title: String? = null,
        description: String? = null,
        properties: LinkedHashSet<Property<*>> = linkedSetOf(),
        commands: LinkedHashSet<Command> = linkedSetOf(),
    ) : this(version, Command(name, title, description, properties), commands)

    init {
        requireNotBlank(Program::version)
    }

    fun input(): String? =
        BufferedReader(System.`in`.reader()).use {
            if (it.ready().out()) it.readText()
            else null
        }

    fun parse(args: Array<String>): Command {
        // Convert to canonical form "-abc param --long value p1 p2" to "-a -b -c=param --long=value p1 p2"
        // Split properties from commands / parameters
        // Get command (first parameters)
        // Group properties
        TODO()
    }

    private fun canonical(args: Array<String>): List<String> = emptyList()

    private fun commands(): Map<String, Command> {
        TODO()
    }
}
