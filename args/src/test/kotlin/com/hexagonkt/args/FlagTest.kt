package com.hexagonkt.args

import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class FlagTest {

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
    }
}
