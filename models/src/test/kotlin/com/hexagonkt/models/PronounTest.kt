package com.hexagonkt.models

import com.hexagonkt.models.Pronoun.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

internal class PronounTest {

    @Test fun `Pronoun contains all valid options`() {
        assertContentEquals(arrayOf(FEMININE, MASCULINE, NEUTRAL), Pronoun.values())
    }
}
