package com.hexagonkt.models

import com.hexagonkt.helpers.parseLocale
import kotlin.test.Test
import kotlin.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class CountryTest {

    @Test fun `Country is created properly`() {
        assertEquals(parseLocale("US"), Country("US").locale)
        assertEquals(parseLocale("_ES"), Country("ES").locale)
    }

    @Test fun `Invalid countries raise errors`() {
        assertFailsWith<IllegalArgumentException> { Country("_US") }
        assertFailsWith<IllegalArgumentException> { Country("USA") }
        assertFailsWith<IllegalArgumentException> { Country("") }
        assertFailsWith<IllegalArgumentException> { Country("us") }
        assertFailsWith<IllegalArgumentException> { Country("es") }
    }
}
