package com.hexagonkt.models

import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class GenderTest {

    @Test
    fun `Gender contains all valid options`() {
        assertContentEquals(
            arrayOf(
                Gender.WOMAN,
                Gender.MAN,
                Gender.LESBIAN,
                Gender.GAY,
                Gender.TRANSGENDER_WOMAN,
                Gender.TRANSGENDER_MAN,
                Gender.BISEXUAL,
                Gender.INTERSEXUAL,
                Gender.QUEER,
                Gender.AGENDER,
                Gender.OTHER
            ),
            Gender.values()
        )
    }
}
