package com.hexagonkt.args

import com.hexagonkt.args.Option.Companion.allowedTargetTypes
import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class OptionTest {

    @Test fun `Options with null optional values are correct`() {
        assertEquals(Option(Boolean::class, 'b'), Option(Boolean::class, 'b', longName = null))
        assertEquals(Option(Boolean::class, 'b'), Option(Boolean::class, 'b', description = null))
    }

    @Test fun `Invalid options raise errors`() {
        listOf('#', ' ').forEach {
            assertEquals(
                "Short name must be a letter or a digit: $it",
                assertFailsWith<IllegalArgumentException> { Option(String::class, it) }.message
            )
        }

        assertEquals(
            "Type ${Regex::class} not in allowed types: $allowedTargetTypes",
            assertFailsWith<IllegalArgumentException> { Option(Regex::class, 'a') }.message
        )

        listOf("", " ", " b ", "b").forEach {
            val e = assertFailsWith<IllegalArgumentException> { Option(String::class, 'a', it) }
            val message = e.message ?: ""
            assertEquals("Long name must be at least two characters: $it", message)
        }

        listOf("", " ", "  ").forEach {
            val e = assertFailsWith<IllegalArgumentException> {
                Option(String::class, 'a', description = it)
            }
            val message = e.message ?: ""
            assertEquals("Description cannot be blank", message)
        }
    }
}
