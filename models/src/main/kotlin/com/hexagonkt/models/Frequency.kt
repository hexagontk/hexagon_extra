package com.hexagonkt.models

import com.hexagonkt.core.toTotalDays
import java.time.Period

data class Frequency(
    val times: Period,
    val interval: Period,
) {
    init {
        require(times.toTotalDays() <= interval.toTotalDays()) {
            "Invalid frequency, times ($times) must be smaller than interval ($interval)"
        }
    }
}
