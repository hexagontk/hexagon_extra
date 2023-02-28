package com.hexagonkt.args

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

inline fun <reified T : Throwable> assertFailsWithMessage(
    message: String? = null, block: () -> Unit
): T {
    val e = assertFailsWith<T> {
        block()
    }

    if (message != null)
        assertEquals(message, e.message)

    return e
}

fun assertIllegalArgument(message: String? = null, block: () -> Unit) {
    assertFailsWithMessage<IllegalArgumentException>(message, block)
}

fun assertIllegalState(message: String? = null, block: () -> Unit) {
    assertFailsWithMessage<IllegalStateException>(message, block)
}
