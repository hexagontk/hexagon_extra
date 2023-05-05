package com.hexagonkt.args

import java.io.File
import java.net.URL
import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class OptionTest {

    @Test fun `Options with null optional values are correct`() {
        assertEquals(
            Option<Boolean>('b', defaultValue = true),
            Option<Boolean>('b', name = null, defaultValue = true)
        )
        assertEquals(
            Option<Boolean>('b', defaultValue = true),
            Option<Boolean>('b', description = null, defaultValue = true)
        )
        assertEquals(
            Option<Boolean>(name = "name", defaultValue = true),
            Option<Boolean>(null, "name", defaultValue = true)
        )
    }

    @Test fun `Invalid options raise errors`() {
        listOf('#', ' ').forEach {
            assertEquals(
                "Names must comply with ${Option.optionRegex} regex: [$it]",
                assertFailsWith<IllegalArgumentException> { Option<String>(it) }.message
            )
        }

        assertFailsWith<IllegalArgumentException> { Option<Regex>('n', "name") }
            .message.let { assert(it?.contains("not in allowed types") ?: false) }

        setOf("", " ", "Ab", "ab_c").forEach { n ->
            assertFailsWith<IllegalArgumentException> { Option<String>('o', n) }
                .message.let {
                    assert(it?.contains("Names must comply with") ?: false)
                }
        }

        val message = "Option regex can only be used for 'string' type: Int"
        assertIllegalArgument(message) { Option<Int>('n', "name", regex = Regex(".*")) }

        val e = assertFailsWith<IllegalArgumentException> {
            Option<String>(name = "name", regex = Regex("A"), defaultValues = listOf("a"))
        }
        assert(e.message?.contains("Default value should match the 'A' regex: a") ?: false)

        assertFailsWith<IllegalArgumentException> { Option<Int>('n', "name", " ") }
    }

    @Test fun `Options can add values`() {
        assertEquals(
            File("/foo/bar"),
            Option<File>('o', "one").addValue("/foo/bar").values.first()
        )

        assertEquals(
            listOf(1, 2),
            Option<Int>('i', "int", multiple = true).addValue("1").addValue("2").values
        )

        assertIllegalArgument("Option 'i' can only have one value: [1, 2]") {
            Option<Int>('i', "int").addValue("1").addValue("2").values
        }

        assertIllegalState("Option 't' of type 'URL' can not hold '0'") {
            Option<URL>('t', "two").addValue("0").values.first()
        }

        assertIllegalState("Option 't' of type 'Int' can not hold '/foo/bar'") {
            Option<Int>('t', "two").addValue("/foo/bar").values.first()
        }
    }
}
