package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command
import com.hexagonkt.args.Option
import com.hexagonkt.args.Parameter
import com.hexagonkt.args.Program
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ProgramFormatterTest {

    private val formatter = ProgramFormatter()

    @Test fun `Command summary is created properly`() {
        val prg = Program(
            "cmd",
            "v1.0.0",
            "CMD Title",
            "Description of the cmd command",
            setOf(
                Option(String::class, 'n', "name"),
                Parameter(String::class, "value"),
            ),
            setOf(
                Command("edit", "Edit config"),
                Command("config", "Display config"),
            )
        )

        assertEquals("cmd - CMD Title (version v1.0.0)\n\nDescription of the cmd command", formatter.summary(prg))
        assertEquals("cmd [-n STRING] [<value>]", formatter.definition(prg))
        val detail = """
            COMMANDS:
              edit     Edit config
              config   Display config

            PARAMETERS:
              <value>   [STRING]

            OPTIONS:
              -n, --name STRING   [STRING]
        """.trimIndent().trim()
        assertEquals(detail, formatter.detail(prg))
    }
}
