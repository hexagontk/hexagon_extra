package com.hexagonkt.args.formatter

import com.hexagonkt.args.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ProgramFormatterTest {

    private val formatter = ProgramFormatter()

    @Test fun `Program summary is created properly`() {
        assertEquals(
            "cmd - CMD Title (version v1.0)\n\nDescription of the cmd command",
            formatter.summary(Program("cmd", "v1.0", "CMD Title", "Description of the cmd command"))
        )
        assertEquals(
            "cmd - CMD Title (version v1.0)",
            formatter.summary(Program("cmd", "v1.0", "CMD Title"))
        )
        assertEquals(
            "cmd (version v1.0)",
            formatter.summary(Program("cmd", "v1.0"))
        )
        assertEquals(
            "cmd",
            formatter.summary(Program("cmd"))
        )
    }

    @Test fun `Program formatter fails on definition and detail`() {
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

        assertEquals("USAGE:\n  ", formatter.definition(prg))
        assertIllegalState("Unsupported operation") { formatter.detail(prg) }
    }
}
