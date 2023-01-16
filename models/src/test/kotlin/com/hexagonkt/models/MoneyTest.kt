package com.hexagonkt.models

import kotlin.test.Test
import java.util.*
import kotlin.test.assertEquals

internal class MoneyTest {

    @Test fun `Entity can be read and written from maps`() {
        assertEquals(Money(10.0, Currency.getInstance("EUR")), Money(10, "EUR"))
        assertEquals(Money(10.5, Currency.getInstance("EUR")), Money(10.5, "EUR"))
        assertEquals(Money(11, Currency.getInstance("EUR")), Money(11, Currency.getInstance("EUR")))
    }
}
