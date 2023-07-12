package com.hexagonkt.processor

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

internal class FieldTest {

    data class SampleField(@Deprecated("It is deprecated") val property: String)

    @Test
    fun `Field returns deprecated annotation`() {
        val data = Type(SampleField::class)
        val field = data.fields.first()
        assertNotNull(field.deprecated)
    }
}
