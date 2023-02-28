package com.hexagonkt.models

import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class PhoneTest {

    @Test
    fun `Valid phones can be created`() {
        Phone("6543210", "+34", landLine = true, description = "Personal").apply {
            assertEquals("6543210", number)
            assertEquals("+34", countryCode)
            assertEquals(true, landLine)
            assertEquals("Personal", description)
        }

        Phone("6543210", "+34").apply {
            assertEquals("6543210", number)
            assertEquals("+34", countryCode)
            assertNull(landLine)
            assertNull(description)
        }

        Phone("6543210").apply {
            assertEquals("6543210", number)
            assertNull(countryCode)
            assertNull(landLine)
            assertNull(description)
        }
    }

    @Test
    fun `Invalid phones throw exceptions on creation`() {
        assertFailsWith<IllegalArgumentException> { Phone(" ", "+34") }
        assertFailsWith<IllegalArgumentException> { Phone("6543210", " ") }
    }
}
