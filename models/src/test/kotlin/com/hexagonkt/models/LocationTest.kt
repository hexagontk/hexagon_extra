package com.hexagonkt.models

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LocationTest {

    @Test
    fun `Location objects can be created`() {
        val location = Location(0.1, 0.2)
        assertEquals(0.1, location.longitude)
        assertEquals(0.2, location.latitude)
    }
}
