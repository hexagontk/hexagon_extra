package com.hexagonkt.args

import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class ParameterTest {

    @Test fun `Invalid parameters fail with exceptions`() {
        assertFailsWith<IllegalArgumentException> { Parameter(Regex::class, "name") }
            .message.let { assert(it?.contains("not in allowed types") ?: false) }

        setOf("", " ", "a", "Ab", "ab_c").forEach { n ->
            assertFailsWith<IllegalArgumentException> { Parameter(String::class, n) }
                .message.let { assert(it?.contains("Names must comply with") ?: false) }
        }

        assertFailsWith<IllegalArgumentException> { Parameter(Int::class, "name", regex = Regex(".*")) }
            .message.let { assert(it?.contains("Regex can only be used for 'string'") ?: false) }

        assertFailsWith<IllegalArgumentException> {
            Parameter(String::class, name = "name", regex = Regex("A"), values = listOf("a"))
        }
        .message.let { assert(it?.contains("Value should match the 'A' regex: a") ?: false) }

        assertFailsWith<IllegalArgumentException> { Parameter(Int::class, "name", " ") }
    }
}
