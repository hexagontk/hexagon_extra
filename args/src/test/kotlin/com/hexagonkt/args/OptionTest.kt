package com.hexagonkt.args

import java.io.File
import java.net.URL
import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class OptionTest {

    @Test fun `Options with null optional values are correct`() {
        assertEquals(Option(Boolean::class, 'b'), Option(Boolean::class, 'b', name = null))
        assertEquals(Option(Boolean::class, 'b'), Option(Boolean::class, 'b', description = null))
    }

    @Test fun `Invalid options raise errors`() {
        listOf('#', ' ').forEach {
            assertEquals(
                "Names must comply with ${Option.optionRegex} regex: [$it]",
                assertFailsWith<IllegalArgumentException> { Option(String::class, it) }.message
            )
        }

        assertFailsWith<java.lang.IllegalArgumentException> { Option(Regex::class, 'n', "name") }
            .message.let { assert(it?.contains("not in allowed types") ?: false) }

        setOf("", " ", "Ab", "ab_c").forEach { n ->
            assertFailsWith<IllegalArgumentException> { Option(String::class, 'o', n) }
                .message.let {
                    assert(it?.contains("Names must comply with") ?: false)
                }
        }

        val message = "Option regex can only be used for 'string' type: Int"
        assertFailsWithMessage<IllegalArgumentException>(message) {
            Option(Int::class, 'n', "name", regex = Regex(".*"))
        }

        val e = assertFailsWith<IllegalArgumentException> {
            Option(String::class, name = "name", regex = Regex("A"), values = listOf("a"))
        }
        assert(e.message?.contains("Value should match the 'A' regex: a") ?: false)

        assertFailsWith<IllegalArgumentException> { Option(Int::class, 'n', "name", " ") }
    }

    @Test fun `Options can add values`() {
        assertEquals(File("/foo/bar"), Option(File::class, 'o', "one").addValue("/foo/bar").value)
        assertEquals(
            listOf(1, 2),
            Option(Int::class, 'i', "int", multiple = true).addValue("1").addValue("2").values
        )

        val message = "Option 'i' can only have one value: [1, 2]"
        assertFailsWithMessage<IllegalArgumentException>(message) {
            Option(Int::class, 'i', "int").addValue("1").addValue("2").values
        }

        val message1 = "Option 't' of type 'URL' can not hold the '0' value"
        assertFailsWithMessage<IllegalStateException>(message1) {
            Option(URL::class, 't', "two").addValue("0").value
        }

        val message2 = "Option 't' of type 'Int' can not hold the '/foo/bar' value"
        assertFailsWithMessage<IllegalStateException>(message2) {
            Option(Int::class, 't', "two").addValue("/foo/bar").value
        }
    }
}
