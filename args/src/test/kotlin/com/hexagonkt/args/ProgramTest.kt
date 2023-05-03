package com.hexagonkt.args

import com.hexagonkt.args.Property.Companion.HELP
import com.hexagonkt.args.Property.Companion.VERSION
import com.hexagonkt.helpers.CodedException
import java.io.ByteArrayInputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ProgramTest {

    @Test fun `Invalid programs raise errors`() {
        assertIllegalArgument { Program("program", " ") }
    }

    @Test fun `Program is created properly`() {
        Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things.",
            properties = setOf(
                Flag('f', "flag"),
                Option(String::class, 'o', "option"),
                Parameter(Int::class, "number")
            )
        )
    }

    @Test fun `Program input can be retrieved`() {
        val program = Program("program")
        val sin = System.`in`
        val sample = "sample input"
        System.setIn(ByteArrayInputStream(sample.toByteArray()))
        assertEquals(sample, program.input())
        assertNull(program.input())
        System.setIn(sin)
    }

    @Test fun `Program parses arguments`() {
        Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things.",
            properties = setOf(
                Flag('f', "flag"),
                Option(String::class, 'o', "option"),
                Parameter(Int::class, "number")
            )
        ).apply {
            assertValues(listOf(true, "opt", 54), "-f -o opt 54")
        }
    }

    @Test fun `Program handles standard flags`() {
        val program = Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things.",
            properties = setOf(
                VERSION,
                HELP,
                Flag('f', "flag"),
                Option(String::class, 'o', "option"),
                Parameter(Int::class, "number")
            )
        )

        val v = "program - Sample program (version 1.0.0)\n\nA simple program that does things."
        assertFailsWithMessage<CodedException>(v) { program.process(listOf("-v")) }
        assertFailsWithMessage<CodedException>(v) { program.process(listOf("--version")) }

        val h = """
            program - Sample program (version 1.0.0)

            A simple program that does things.

            USAGE:
              program [-v] [-h] [-f] [-o STRING] [<number>]

            PARAMETERS:
              <number>   [INT]

            OPTIONS:
              -o, --option STRING   [STRING]

            FLAGS:
              -v, --version   Show the program's version along its description
              -h, --help      Display detailed information on running this program
              -f, --flag
        """.trimIndent().trim()
        assertFailsWithMessage<CodedException>(h) { program.process(listOf("-h")) }
        assertFailsWithMessage<CodedException>(h) { program.process(listOf("--help")) }
    }

    @Test fun `Program handles standard flags with required properties`() {
        val program = Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things.",
            properties = setOf(
                VERSION,
                HELP,
                Flag('f', "flag"),
                Option(String::class, 'o', "option"),
                Parameter(Int::class, "number", optional = false)
            )
        )

        val v = "program - Sample program (version 1.0.0)\n\nA simple program that does things."
        assertFailsWithMessage<CodedException>(v) { program.process(listOf("-v")) }
        assertFailsWithMessage<CodedException>(v) { program.process(listOf("--version")) }

        val h = """
            program - Sample program (version 1.0.0)

            A simple program that does things.

            USAGE:
              program [-v] [-h] [-f] [-o STRING] <number>

            PARAMETERS:
              <number>   INT

            OPTIONS:
              -o, --option STRING   [STRING]

            FLAGS:
              -v, --version   Show the program's version along its description
              -h, --help      Display detailed information on running this program
              -f, --flag
        """.trimIndent().trim()
        assertFailsWithMessage<CodedException>(h) { program.process(listOf("-h")) }
        assertFailsWithMessage<CodedException>(h) { program.process(listOf("--help")) }
    }

    @Test fun `Program handles errors`() {
        val program = Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things.",
            properties = setOf(
                Flag('f', "flag"),
                Option(String::class, 'o', "option"),
                Parameter(Int::class, "number")
            )
        )

        val h = """
            USAGE:
              program [-f] [-o STRING] [<number>]

            Use the --help option (-h) to get more information""".trimIndent()

        assertFailsWithMessage<CodedException>("Unknown argument at position 2: 2\n\n$h") {
            program.process(listOf("1", "2"))
        }
        assertFailsWithMessage<CodedException>("Option 'none' not found\n\n$h") {
            program.process(listOf("--none"))
        }
    }

    @Test fun `Program handles subcommands`() {
        fun Program.check() {
            assertValues(listOf(true, "val"), "cmd -12val")
            assertValues(listOf(true, "val"), "cmd -12 val")
            assertValues(listOf(true, "val"), "cmd -12=val")
            assertValues(listOf(true, "val"), "cmd -1 -2val")
            assertValues(listOf(true, "val"), "cmd -1 -2 val")
            assertValues(listOf(true, "val"), "cmd -1 -2=val")
        }

        Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things.",
            properties = setOf(VERSION, HELP),
            commands = setOf(
                Command(
                    name = "cmd",
                    properties = setOf(
                        Flag('1', "first"),
                        Option(String::class, '2', "second"),
                    )
                )
            )
        ).apply(Program::check)

        Program(
            name = "program",
            commands = setOf(
                Command(
                    name = "cmd",
                    properties = setOf(
                        Flag('1', "first"),
                        Option(String::class, '2', "second"),
                    )
                )
            )
        ).apply(Program::check)
    }

    @Test fun `Program parse command line parameters`() {
        val program = Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things.",
            properties = setOf(VERSION, HELP),
            commands = setOf(
                Command(
                    name = "cmd",
                    title = "A sample subcommand",
                    description = "The subcommand description.",
                    properties = setOf(
                        HELP,
                        Flag('1', "first"),
                        Option(String::class, '2', "second"),
                    )
                )
            )
        )

        assert(program.parse(arrayOf("cmd", "-1")).flags.first().values.first())
    }

    @Test fun `Program describes subcommands`() {
        val program = Program(
            name = "program",
            version = "1.0.0",
            title = "Sample program",
            description = "A simple program that does things",
            properties = setOf(VERSION, HELP),
            commands = setOf(
                Command(
                    name = "cmd",
                    title = "A sample subcommand",
                    description = "The subcommand description",
                    properties = setOf(
                        HELP,
                        Flag('1', "first"),
                        Option(String::class, '2', "second"),
                    )
                )
            )
        )

        val v = "program - Sample program (version 1.0.0)\n\nA simple program that does things"
        assertFailsWithMessage<CodedException>(v) { program.process(listOf("-v")) }
        assertFailsWithMessage<CodedException>(v) { program.process(listOf("--version")) }

        val h = """
            program - Sample program (version 1.0.0)

            A simple program that does things

            USAGE:
              program [-v] [-h]

            COMMANDS:
              cmd   A sample subcommand

            FLAGS:
              -v, --version   Show the program's version along its description
              -h, --help      Display detailed information on running this program
        """.trimIndent().trim()
        assertFailsWithMessage<CodedException>(h) { program.process(listOf("-h")) }
        assertFailsWithMessage<CodedException>(h) { program.process(listOf("--help")) }

        val c = """
            cmd - A sample subcommand

            The subcommand description

            USAGE:
              program cmd [-h] [-1] [-2 STRING]

            OPTIONS:
              -2, --second STRING   [STRING]

            FLAGS:
              -h, --help    Display detailed information on running this program
              -1, --first
        """.trimIndent().trim()
        assertFailsWithMessage<CodedException>(c) { program.process(listOf("cmd", "-h")) }
        assertFailsWithMessage<CodedException>(c) { program.process(listOf("cmd", "--help")) }
    }

    private fun Program.assertValues(values: List<*>, args: String) {
        assertValues(values, args.split(' ').filter(String::isNotBlank))
    }

    private fun Program.assertValues(values: List<*>, args: List<String>) {
        assertEquals(values, parse(args).properties.flatMap { it.values })
    }
}
