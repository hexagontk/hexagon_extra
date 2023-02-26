package com.hexagonkt.args

import com.hexagonkt.helpers.out
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class CommandTest {

    @Test fun `Invalid command definitions throw errors`() {
        assertFailsWith<IllegalArgumentException> { Command(" ") }
        assertFailsWith<IllegalArgumentException> { Command("cmd", title = " ") }
        assertFailsWith<IllegalArgumentException> { Command("cmd", description = " ") }

        assertIllegalArgument("Only the last positional parameter can be multiple") {
            Command(
                name = "cmd",
                properties = setOf(
                    Parameter(String::class, "first", multiple = true),
                    Parameter(Int::class, "second")
                )
            )
        }
    }

    @Test fun `Command creates a map of options`() {
        val option = Option(File::class, 's', "assets")
        val serve = Command("serve", properties = setOf(option))

        assertEquals(mapOf("s" to option, "assets" to option), serve.propertiesMap)
        assertEquals(mapOf("s" to option, "assets" to option), serve.optionsMap)
        assertEquals(emptyMap(), serve.parametersMap)

        val last = Parameter(String::class, "last", multiple = true)
        val cmd = Command("cmd", properties = setOf(last))

        assertEquals(mapOf("last" to last), cmd.propertiesMap)
        assertEquals(emptyMap(), cmd.optionsMap)
        assertEquals(mapOf("last" to last), cmd.parametersMap)

        val first = Parameter(String::class, "first")
        val scr = Command("scr", properties = setOf(first, last))

        assertEquals(mapOf("first" to first, "last" to last), scr.propertiesMap)
        assertEquals(emptyMap(), scr.optionsMap)
        assertEquals(mapOf("first" to first, "last" to last), scr.parametersMap)
    }

    @Test fun `Command can have options and parameters`() {
        val option = Option(File::class, 's', "assets")
        val last = Parameter(String::class, "last", multiple = true)
        val first = Parameter(String::class, "first")
        val serve = Command("serve", properties = setOf(option, first, last))

        val options = mapOf("s" to option, "assets" to option)
        val parameters = mapOf("first" to first, "last" to last)

        assertEquals(options + parameters, serve.propertiesMap)
        assertEquals(options, serve.optionsMap)
        assertEquals(parameters, serve.parametersMap)
    }

    @Test fun `Tree of commands is flattened properly`() {
        val a = Command("a",
            subcommands = setOf(
                Command("aa", subcommands = setOf(Command("aaa"), Command("aab"))),
                Command("ab", subcommands = setOf(Command("aba"), Command("abb"))),
            )
        )

        val subcommandsNames = a.subcommandsMap.keys

        assertEquals(
            setOf(
                "a aa",
                "a aa aaa",
                "a aa aab",
                "a ab",
                "a ab aba",
                "a ab abb"
            ),
            subcommandsNames
        )

        subcommandsNames.sortedByDescending { it.count { c -> c == ' ' } }.out()
    }

    @Test fun `Find command works in a tree of commands`() {
        val aaa = Command("aaa")
        val aab = Command("aab")
        val aa = Command("aa", subcommands = setOf(aaa, aab))
        val aba = Command("aba")
        val abb = Command("abb")
        val ab = Command("ab", subcommands = setOf(aba, abb))
        val a = Command("a", subcommands = setOf(aa, ab))

        assertEquals(aa, a.findCommand(arrayOf("aa")))
        assertEquals(ab, a.findCommand(arrayOf("ab")))
        assertEquals(aaa.copy(name = "aa ${aaa.name}"), a.findCommand(arrayOf("aa", "aaa")))
        assertEquals(aba.copy(name = "ab ${aba.name}"), a.findCommand(arrayOf("ab", "aba")))
        assertEquals(aab.copy(name = "aa ${aab.name}"), a.findCommand(arrayOf("aa", "aab")))
        assertEquals(abb.copy(name = "ab ${abb.name}"), a.findCommand(arrayOf("ab", "abb")))
        assertEquals(a, a.findCommand(arrayOf("ax")))
    }

    @Test fun `Commands can parse their own options`() {
        val cmd = Command(
            name = "cmd",
            properties = setOf(
                Flag('1', "first"),
                Option(String::class, '2', "second"),
                Parameter(Int::class, "number"),
            )
        )

        assertEquals(listOf(true), cmd.parse(listOf("--first")).flags.first().values)
        assertEquals(listOf(true), cmd.parse(listOf("-1")).flags.first().values)
        assertEquals(listOf("val"), cmd.parse(listOf("-2val")).options.first().values)
        assertEquals(listOf("val"), cmd.parse(listOf("-2=val")).options.first().values)
        assertEquals(listOf("val"), cmd.parse(listOf("-12=val")).options.first().values)
        assertEquals(listOf("val"), cmd.parse(listOf("--second=val")).options.first().values)
        assertEquals(listOf("val"), cmd.parse(listOf("--second", "val")).options.first().values)
        assertEquals(listOf(42), cmd.parse(listOf("42")).parameters.first().values)
    }

    @Test fun `Commands throw errors with invalid arguments`() {
        val cmd = Command(
            name = "cmd",
            properties = setOf(
                Flag('1', "first"),
                Option(String::class, '2', "second"),
                Parameter(Int::class, "number"),
            )
        )

        cmd.assertIllegalState("Option 'none' not found", "--none")
        cmd.assertIllegalState("Option 'Z' not found", "-Z")

        cmd.assertIllegalState("Unknown argument at position 2: 42", "41 42")
    }

    private fun Command.assertIllegalArgument(message: String, args: List<String>) {
        assertFailsWithMessage<IllegalArgumentException>(message) { parse(args) }
    }

    private fun Command.assertIllegalState(message: String, args: List<String>) {
        assertFailsWithMessage<IllegalStateException>(message) { parse(args) }
    }

    private fun Command.assertIllegalArgument(message: String, args: String) {
        assertIllegalArgument(message, args.split(' ').filter(String::isNotBlank))
    }

    private fun Command.assertIllegalState(message: String, args: String) {
        assertIllegalState(message, args.split(' ').filter(String::isNotBlank))
    }
}
