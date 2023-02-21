package com.hexagonkt.args

import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ParameterTest {

    @Test fun `Invalid parameters fail with exceptions`() {
        assertFailsWith<IllegalArgumentException> { Parameter(Regex::class, "name") }
            .message.let { assert(it?.contains("not in allowed types") ?: false) }

        setOf("", " ", "a", "Ab", "ab_c").forEach { n ->
            assertFailsWith<IllegalArgumentException> { Parameter(String::class, n) }
                .message.let { assert(it?.contains("Names must comply with") ?: false) }
        }

        val message = "Parameter regex can only be used for 'string' type: Int"
        assertFailsWithMessage<IllegalArgumentException>(message) {
            Parameter(Int::class, "name", regex = Regex(".*"))
        }

        val e = assertFailsWith<IllegalArgumentException> {
            Parameter(String::class, name = "name", regex = Regex("A"), values = listOf("a"))
        }
        assert(e.message?.contains("Value should match the 'A' regex: a") ?: false)

        assertFailsWith<IllegalArgumentException> { Parameter(Int::class, "name", " ") }
    }

    @Test fun `Parameters can add values`() {
        assertEquals(File("/foo/bar"), Parameter(File::class, "one").addValue("/foo/bar").value)
        assertEquals(
            listOf(1, 2),
            Parameter(Int::class, "int", multiple = true).addValue("1").addValue("2").values
        )

        val message = "Parameter 'int' can only have one value: [1, 2]"
        assertFailsWithMessage<IllegalArgumentException>(message) {
            Parameter(Int::class, "int").addValue("1").addValue("2").values
        }

        val message1 = "Parameter 'two' of type 'URL' can not hold the '0' value"
        assertFailsWithMessage<IllegalStateException>(message1) {
            Parameter(URL::class, "two").addValue("0").value
        }

        val message2 = "Parameter 'two' of type 'Int' can not hold the '/foo/bar' value"
        assertFailsWithMessage<IllegalStateException>(message2) {
            Parameter(Int::class, "two").addValue("/foo/bar").value
        }
    }
}
