package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command
import com.hexagonkt.args.Formatter
import com.hexagonkt.args.Program

data class ProgramFormatter(
    val commandFormatter: Formatter<Command> = CommandFormatter(),
) : Formatter<Program> {

    override fun summary(component: Program): String {
        val title = listOfNotNull(
            component.command.name,
            component.command.title?.let { "- $it" },
            component.version?.let { "(version ${component.version})" }
        )
        .joinToString(" ")

        return "$title\n\n${component.command.description}"
    }

    override fun definition(component: Program): String =
        commandFormatter.definition(component.command)

    override fun detail(component: Program): String =
        commandFormatter.detail(component.command)
}
