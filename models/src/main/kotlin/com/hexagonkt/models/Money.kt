package com.hexagonkt.models

import java.util.*

data class Money(val amount: Double, val currency: Currency) {

    constructor(amount: Double, currencyCode: String) :
        this(amount, Currency.getInstance(currencyCode))

    constructor(amount: Int, currency: Currency) :
        this(amount.toDouble(), currency)

    constructor(amount: Int, currencyCode: String) :
        this(amount.toDouble(), Currency.getInstance(currencyCode))
}
