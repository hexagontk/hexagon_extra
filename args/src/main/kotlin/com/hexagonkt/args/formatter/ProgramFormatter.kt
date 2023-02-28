package com.hexagonkt.args.formatter

import com.hexagonkt.args.Formatter
import com.hexagonkt.args.Program

data class ProgramFormatter(
    val titleSeparator: String = "-",
    val versionPrefix: String = "(version ",
    val versionSuffix: String = ")"
) : Formatter<Program> {

    override fun summary(component: Program): String {
        val title = listOfNotNull(
            component.command.name,
            component.command.title?.let { "$titleSeparator $it" },
            component.version?.let { "$versionPrefix${component.version}$versionSuffix" }
        )
        .joinToString(" ")

        return if (component.command.description == null) title
        else "$title\n\n${component.command.description}"
    }

    override fun definition(component: Program): String =
        error("Unsupported operation")

    override fun detail(component: Program): String =
        error("Unsupported operation")
}
