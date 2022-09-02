package com.hexagonkt.models

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class AddressTest {

    @Test
    fun `Valid addresses can be created`() {
        Address("28021", "ES", "C/", "Madrid", "Provincia", "Comunidad").apply {
            assertEquals("C/", addressLine)
            assertEquals("28021", postalCode)
            assertEquals("ES", countryIso)
            assertEquals("Madrid", locality)
            assertEquals("Provincia", subregion)
            assertEquals("Comunidad", region)
        }

        Address("28021", "ES", "C/").apply {
            assertEquals("C/", addressLine)
            assertEquals("28021", postalCode)
            assertEquals("ES", countryIso)
            assertNull(locality)
            assertNull(subregion)
            assertNull(region)
        }

        Address("28021", "ES").apply {
            assertEquals("28021", postalCode)
            assertEquals("ES", countryIso)
            assertNull(addressLine)
            assertNull(locality)
            assertNull(subregion)
            assertNull(region)
        }
    }

    @Test
    fun `Invalid addresses throw exceptions on creation`() {
        assertFailsWith<IllegalArgumentException> { Address("28021", "ES", " ") }
        assertFailsWith<IllegalArgumentException> { Address(" ", "ES", "C/") }
        assertFailsWith<IllegalArgumentException> { Address("28021", " ", "C/") }
        assertFailsWith<IllegalArgumentException> { Address("28021", "ES", "C/", " ") }
        assertFailsWith<IllegalArgumentException> { Address("28021", "ES", "C/", "Madrid", " ") }
        assertFailsWith<IllegalArgumentException> {
            Address("28021", "ES", "C/", "Madrid", "Madrid", " ")
        }
    }
}
