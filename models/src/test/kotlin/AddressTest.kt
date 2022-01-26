package com.hexagonkt.models

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class AddressTest {

    @Test
    fun `Valid addresses can be created`() {
        Address("C/", "28021", "ES", "Madrid", "Provincia", "Comunidad").apply {
            assertEquals("C/", addressLine)
            assertEquals("28021", postalCode)
            assertEquals("ES", countryIso)
            assertEquals("Madrid", locality)
            assertEquals("Provincia", subregion)
            assertEquals("Comunidad", region)
        }

        Address("C/", "28021", "ES").apply {
            assertEquals("C/", addressLine)
            assertEquals("28021", postalCode)
            assertEquals("ES", countryIso)
            assertNull(locality)
            assertNull(subregion)
            assertNull(region)
        }
    }

    @Test
    fun `Invalid addresses throw exceptions on creation`() {
        assertFailsWith<IllegalArgumentException> { Address(" ", "28021", "ES") }
        assertFailsWith<IllegalArgumentException> { Address("C/", " ", "ES") }
        assertFailsWith<IllegalArgumentException> { Address("C/", "28021", " ") }
        assertFailsWith<IllegalArgumentException> { Address("C/", "28021", "ES", " ") }
        assertFailsWith<IllegalArgumentException> { Address("C/", "28021", "ES", "Madrid", " ") }
        assertFailsWith<IllegalArgumentException> {
            Address("C/", "28021", "ES", "Madrid", "Madrid", " ")
        }
    }
}
