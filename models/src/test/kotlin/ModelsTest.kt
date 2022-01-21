package com.hexagonkt.models

import org.junit.jupiter.api.Test
import java.util.Locale.ENGLISH
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class CommonTest {

    @Test fun `Mails are validated properly`() {
        assertFailsWith<IllegalArgumentException> { Email(" ") }
        assertFailsWith<IllegalArgumentException> { Email("foo") }
        assertEquals("foo", Email("foo@bar.com").user)
    }
}

internal class TextTest {

    @Test fun `Text is created and accessed`() {
        Text("en" to "text", "es" to "texto").let {

            assertEquals(it, Text("es" to "texto", "en" to "text"))

            assertEquals("text", it[ENGLISH])

            assertEquals("text", it["en"])
            assertEquals("texto", it["es"])
            assertNull(it["de"])

            assertEquals("text", it.getOrDefault("de"))
            assertEquals("text", it.getOrDefault(ENGLISH))
        }

        Text("text").let {

            assertEquals("text", it[ENGLISH])
            assertEquals("text", it["en"])
            assertNull(it["de"])

            assertEquals("text", it.getOrDefault("de"))
            assertEquals("text", it.getOrDefault(ENGLISH))
        }
    }
}
