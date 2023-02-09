package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command
import com.hexagonkt.args.Formatter
import com.hexagonkt.args.Program

class DefaultFormatter : Formatter {

    override fun overview(program: Program): String = ""
    override fun version(program: Program): String = ""
    override fun help(program: Program): String = ""
    override fun help(command: Command): String = ""

    fun Command.summary(): String =
        listOfNotNull(
            name,
            title?.let { "- $it" },
        ).joinToString(" ")

    fun Program.summary(): String =
        listOfNotNull(
            command.name,
            command.title?.let { "- $it" },
            version?.let { "(version $version)" }
        ).joinToString(" ")

    fun Program.usage(): String =
        "Usage: ${command.name}"
}
