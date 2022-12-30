package com.hexagonkt.models

import com.hexagonkt.core.parseLocale
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class LanguageTest {

    @Test fun `Language is created properly`() {
        assertEquals(parseLocale("en"), Language("en").locale)
        assertEquals(parseLocale("es_"), Language("es").locale)
        assertEquals(parseLocale("en_US"), Language("en_US").locale)
    }

    @Test fun `Invalid languages raise errors`() {
        assertFailsWith<IllegalArgumentException> { Language("_US") }
        assertFailsWith<IllegalArgumentException> { Language("USA") }
        assertFailsWith<IllegalArgumentException> { Language("") }
        assertFailsWith<IllegalArgumentException> { Language("US") }
        assertFailsWith<IllegalArgumentException> { Language("ES") }
    }
}
