package com.hexagonkt.models

import java.time.Period

// TODO Take care of both periods to make sure times is a smaller amount of time than interval
data class Frequency(
    val times: Period,
    val interval: Period,
)
