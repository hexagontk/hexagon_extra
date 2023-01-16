package com.hexagonkt.models

import kotlin.test.Test
import kotlin.IllegalArgumentException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class TextTest {

    @Test fun `Texts cannot be created with invalid data`() {
        assertFailsWith<IllegalArgumentException> { Text() }
        assertFailsWith<IllegalArgumentException> { Text("en" to "") }
        assertFailsWith<IllegalArgumentException> { Text("en" to " ") }
    }

    @Test fun `Text is created and accessed`() {
        Text("en" to "text", "es" to "texto").let {

            assertEquals(it, Text("es" to "texto", "en" to "text"))
            assertEquals(it.hashCode(), Text("es" to "texto", "en" to "text").hashCode())
            assertEquals("{en=text, es=texto}", it.toString())

            assertEquals(it.text, it[Locale.ENGLISH])
            assertEquals("text", it[Locale.ENGLISH])

            assertEquals("text", it["en"])
            assertEquals("texto", it["es"])
            assertNull(it["de"])

            assertEquals("text", it.getOrDefault("de"))
            assertEquals("text", it.getOrDefault(Locale.ENGLISH))
        }

        Text("text").let {

            assertEquals("text", it[Locale.ENGLISH])
            assertEquals("text", it["en"])
            assertNull(it["de"])

            assertEquals("text", it.getOrDefault("de"))
            assertEquals("text", it.getOrDefault(Locale.ENGLISH))
        }
    }
}
