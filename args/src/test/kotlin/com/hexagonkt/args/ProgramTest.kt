package com.hexagonkt.args

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ProgramTest {

    @Test fun `Program is created properly`() {
        assertEquals(listOf(0, 1, 4, 2, 3), listOf(0, 1, 0, 4, 0, 2, 1, 3, 4).toSet().toList())
        assertEquals(listOf(5, 4, 1, 99, 0), setOf(5, 4, 1, 99, 0).toList())
    }
}
