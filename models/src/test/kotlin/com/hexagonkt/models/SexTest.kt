package com.hexagonkt.models

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

internal class SexTest {

    @Test
    fun `Gender contains all valid options`() {
        assertContentEquals(arrayOf(Sex.FEMALE, Sex.MALE, Sex.OTHER), Sex.values())
    }
}
