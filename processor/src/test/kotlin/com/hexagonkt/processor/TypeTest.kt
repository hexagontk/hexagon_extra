package com.hexagonkt.processor

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

internal class TypeTest {

    @Deprecated("It is deprecated") data class SampleClass(val property: String)

    @Test
    @Suppress("DEPRECATION") // Testing deprecation functionality explicitly
    fun `Class returns deprecated annotation`() {
        val data = Type(SampleClass::class)
        assertNotNull(data.deprecated)
    }
}
