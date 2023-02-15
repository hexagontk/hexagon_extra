package com.hexagonkt.models

import com.hexagonkt.helpers.parseLocale
import java.util.*

data class Country(val code: String) {

    val locale: Locale = parseLocale(code)

    init {
        require(locale.language.isBlank())
        require(code.length == 2)
    }
}
