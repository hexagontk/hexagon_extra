package com.hexagonkt.models

import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class PhoneTest {

    @Test
    fun `Valid phones can be created`() {
        Phone("6543210", "+34", landLine = true, personal = true).apply {
            assertEquals("6543210", number)
            assertEquals("+34", countryCode)
            assertEquals(true, landLine)
            assertEquals(true, personal)
        }

        Phone("6543210", "+34").apply {
            assertEquals("6543210", number)
            assertEquals("+34", countryCode)
            assertNull(landLine)
            assertNull(personal)
        }

        Phone("6543210").apply {
            assertEquals("6543210", number)
            assertNull(countryCode)
            assertNull(landLine)
            assertNull(personal)
        }
    }

    @Test
    fun `Invalid phones throw exceptions on creation`() {
        assertFailsWith<IllegalArgumentException> { Phone(" ", "+34") }
        assertFailsWith<IllegalArgumentException> { Phone("6543210", " ") }
    }
}
