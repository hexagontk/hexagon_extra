package com.hexagonkt.models

import com.hexagonkt.core.parsePeriod
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class FrequencyTest {

    @Test fun `Frequencies are checked correctly`() {
        Frequency(parsePeriod("1d"), parsePeriod("2d"))
        Frequency(parsePeriod("1d"), parsePeriod("1d"))
        Frequency(parsePeriod("2y 3m 4w 1d"), parsePeriod("2y 3m 4w 1d"))

        assertIllegalArgument(
            "Invalid frequency, times (P3D) must be smaller than interval (P2D)"
        ) {
            Frequency(parsePeriod("3d"), parsePeriod("2d"))
        }
        assertIllegalArgument(
            "Invalid frequency, times (P2Y3M30D) must be smaller than interval (P2Y3M29D)"
        ) {
            Frequency(parsePeriod("2y 3m 4w 2d"), parsePeriod("2y 3m 4w 1d"))
        }
    }

    private inline fun assertIllegalArgument(
        message: String? = null, block: () -> Unit
    ): IllegalArgumentException =
        assertFailsWith<IllegalArgumentException> { block() }.apply {
            if (message != null)
                assertEquals(message, this.message)
        }
}
