package com.hexagonkt.args.formatter

import com.hexagonkt.args.Program

class ProgramFormatter : ComponentFormatter<Program> {

    override fun summary(component: Program): String =
        listOfNotNull(
            component.command.name,
            component.command.title?.let { "- $it" },
            component.version?.let { "(version ${component.version})" }
        ).joinToString(" ")

    override fun definition(component: Program): String {
        TODO("Not yet implemented")
    }

    override fun detail(component: Program): String {
        TODO("Not yet implemented")
    }

    fun Program.usage(): String =
        "Usage: ${command.name}"
}
