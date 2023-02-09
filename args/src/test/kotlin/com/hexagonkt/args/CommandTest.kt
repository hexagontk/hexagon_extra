package com.hexagonkt.args

import com.hexagonkt.core.out
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

        assertFailsWith<IllegalArgumentException> {
            Command(
                name = "cmd",
                properties = linkedSetOf(
                    Parameter(String::class, "first", multiple = true),
                    Parameter(Int::class, "second")
                )
            )
        }
        .let { assertEquals("Only the last positional parameter can be multiple", it.message) }
    }

    @Test fun `Command creates a map of options`() {
        val option = Option(File::class, 's', "assets")
        val serve = Command("serve", properties = linkedSetOf(option))

        assertEquals(mapOf("s" to option, "assets" to option), serve.propertiesMap)
        assertEquals(mapOf("s" to option, "assets" to option), serve.optionsMap)
        assertEquals(emptyMap(), serve.parametersMap)

        val last = Parameter(String::class, "last", multiple = true)
        val cmd = Command("cmd", properties = linkedSetOf(last))

        assertEquals(mapOf("last" to last), cmd.propertiesMap)
        assertEquals(emptyMap(), cmd.optionsMap)
        assertEquals(mapOf("last" to last), cmd.parametersMap)

        val first = Parameter(String::class, "first")
        val scr = Command("scr", properties = linkedSetOf(first, last))

        assertEquals(mapOf("first" to first, "last" to last), scr.propertiesMap)
        assertEquals(emptyMap(), scr.optionsMap)
        assertEquals(mapOf("first" to first, "last" to last), scr.parametersMap)
    }

    @Test fun `Command can have options and parameters`() {
        val option = Option(File::class, 's', "assets")
        val last = Parameter(String::class, "last", multiple = true)
        val first = Parameter(String::class, "first")
        val serve = Command("serve", properties = linkedSetOf(option, first, last))

        val options = mapOf("s" to option, "assets" to option)
        val parameters = mapOf("first" to first, "last" to last)

        assertEquals(options + parameters, serve.propertiesMap)
        assertEquals(options, serve.optionsMap)
        assertEquals(parameters, serve.parametersMap)
    }

    @Test fun `Tree of commands is flattened properly`() {
        val a = Command("a",
            subcommands = linkedSetOf(
                Command("aa", subcommands = linkedSetOf(Command("aaa"), Command("aab"))),
                Command("ab", subcommands = linkedSetOf(Command("aba"), Command("abb"))),
            )
        )

        val subcommandsNames = a.subcommandsMap().keys

        assertEquals(
            linkedSetOf(
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
}
