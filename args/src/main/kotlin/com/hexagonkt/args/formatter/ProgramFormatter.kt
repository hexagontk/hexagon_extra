package com.hexagonkt.args.formatter

import com.hexagonkt.args.Formatter
import com.hexagonkt.args.Program

data class ProgramFormatter(
    val titleSeparator: String = "-",
    val versionPrefix: String = "(version ",
    val versionSuffix: String = ")",
) : Formatter<Program> {

    override fun summary(component: Program): String {
        val description = component.command.description
        val title = listOfNotNull(
            component.command.name,
            component.command.title?.let { "$titleSeparator $it" },
            component.version?.let { "$versionPrefix${component.version}$versionSuffix" }
        )
        .joinToString(" ")

        return if (description == null) title else "$title\n\n$description"
    }

    override fun definition(component: Program): String =
        "USAGE\n  "

    override fun detail(component: Program): String =
        error("Unsupported operation")
}
