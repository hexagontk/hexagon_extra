package com.hexagonkt.models

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class EmailTest {

    @Test fun `Mails are validated properly`() {
        assertFailsWith<IllegalArgumentException> { Email(" ") }
        assertFailsWith<IllegalArgumentException> { Email("foo") }
        assertEquals("foo", Email("foo@bar.com").user)
    }
}
