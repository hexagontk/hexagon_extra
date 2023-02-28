package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command
import com.hexagonkt.args.Option
import com.hexagonkt.args.Parameter
import com.hexagonkt.args.Property.Companion.HELP
import com.hexagonkt.args.Property.Companion.VERSION
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CommandFormatterTest {

    private val formatter = CommandFormatter()

    @Test fun `Command summary is created properly`() {
        val cmd = Command(
            "cmd",
            "CMD Title",
            "Description of the cmd command",
            setOf(
                HELP,
                VERSION,
                Option(String::class, 'n', "name"),
                Option(String::class, 'o', "output"),
                Parameter(String::class, "source"),
                Parameter(String::class, "target"),
            ),
            setOf(
                Command("edit", "Edit config"),
                Command("config", "Display config"),
            ),
        )

        assertEquals("cmd - CMD Title\n\nDescription of the cmd command", formatter.summary(cmd))
        assertEquals(
            "cmd [-h] [-v] [-n STRING] [-o STRING] [<source>] [<target>]",
            formatter.definition(cmd)
        )

        val detail = """
            COMMANDS:
              edit     Edit config
              config   Display config

            PARAMETERS:
              <source>   [STRING]
              <target>   [STRING]

            OPTIONS:
              -n, --name STRING     [STRING]
              -o, --output STRING   [STRING]

            FLAGS:
              -h, --help      Display detailed information on running this program.
              -v, --version   Show the program's version along its description.
        """.trimIndent().trim()
        assertEquals(detail, formatter.detail(cmd))
    }
}
