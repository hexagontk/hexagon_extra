package com.hexagonkt.models

import java.util.*

data class Money(val amount: Double, val currency: Currency) {

    constructor(amount: Double, currency: String) :
        this(amount, Currency.getInstance(currency))

    constructor(amount: Int, currency: Currency) :
        this(amount.toDouble(), currency)

    constructor(amount: Int, currency: String) :
        this(amount.toDouble(), Currency.getInstance(currency))
}
