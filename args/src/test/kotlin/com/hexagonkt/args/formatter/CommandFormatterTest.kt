package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command
import com.hexagonkt.args.Option
import com.hexagonkt.args.Parameter
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CommandFormatterTest {

    private val formatter = CommandFormatter()

    @Test fun `Command summary is created properly`() {
        val cmd = Command(
            "cmd",
            "CMD Title",
            "Description of the cmd command",
            linkedSetOf(
                Option(String::class, 'n', "name"),
                Parameter(String::class, "value"),
            ),
            linkedSetOf(
                Command("edit"),
                Command("config"),
            ),
        )

        assertEquals("cmd - CMD Title\n\nDescription of the cmd command", formatter.summary(cmd))
        assertEquals("cmd [-n STRING] [<value>]", formatter.definition(cmd))
        val detail = """
            COMMANDS:
              edit
              config

            PARAMETERS:
              <value>   [STRING]

            OPTIONS:
              -n, --name STRING   [STRING]
        """.trimIndent().trim()
        assertEquals(detail, formatter.detail(cmd))
    }
}
