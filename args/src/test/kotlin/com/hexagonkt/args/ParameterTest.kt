package com.hexagonkt.args

import java.io.File
import java.lang.IllegalArgumentException
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ParameterTest {

    @Test fun `Parameters with null optional fields are correct`() {
        assertEquals(
            Parameter(String::class, "str", value = "val"),
            Parameter(String::class, "str", regex = null, value = "val")
        )
        assertEquals(
            Parameter(Boolean::class, "bool", value = true),
            Parameter(Boolean::class, "bool", description = null, value = true)
        )
    }

    @Test fun `Invalid parameters fail with exceptions`() {
        assertFailsWith<IllegalArgumentException> { Parameter(Regex::class, "name") }
            .message.let { assert(it?.contains("not in allowed types") ?: false) }

        setOf("", " ", "a", "Ab", "ab_c").forEach { n ->
            assertFailsWith<IllegalArgumentException> { Parameter(String::class, n) }
                .message.let { assert(it?.contains("Names must comply with") ?: false) }
        }

        assertIllegalArgument("Parameter regex can only be used for 'string' type: Int") {
            Parameter(Int::class, "name", regex = Regex(".*"))
        }

        val e = assertFailsWith<IllegalArgumentException> {
            Parameter(String::class, name = "name", regex = Regex("A"), values = listOf("a"))
        }
        assert(e.message?.contains("Value should match the 'A' regex: a") ?: false)

        assertFailsWith<IllegalArgumentException> { Parameter(Int::class, "name", " ") }
    }

    @Test fun `Parameters can add values`() {
        assertEquals(
            File("/foo/bar"),
            Parameter(File::class, "one").addValue("/foo/bar").values.first()
        )

        assertEquals(
            listOf(1, 2),
            Parameter(Int::class, "int", multiple = true).addValue("1").addValue("2").values
        )

        assertIllegalArgument("Parameter 'int' can only have one value: [1, 2]") {
            Parameter(Int::class, "int").addValue("1").addValue("2").values
        }

        assertIllegalState("Parameter 'two' of type 'URL' can not hold '0'") {
            Parameter(URL::class, "two").addValue("0").values.first()
        }

        assertIllegalState("Parameter 'two' of type 'Int' can not hold '/foo/bar'") {
            Parameter(Int::class, "two").addValue("/foo/bar").values.first()
        }
    }
}
