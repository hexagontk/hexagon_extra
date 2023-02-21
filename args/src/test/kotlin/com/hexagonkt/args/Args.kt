package com.hexagonkt.args

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

inline fun <reified T : Throwable> assertFailsWithMessage(message: String, block: () -> Unit) {
    val e = assertFailsWith<T> {
        block()
    }

    assertEquals(message, e.message)
}
