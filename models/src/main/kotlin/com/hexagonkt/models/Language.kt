package com.hexagonkt.models

import com.hexagonkt.helpers.parseLocale
import java.util.Locale as JavaLocale

data class Language(val code: String) {

    val locale: JavaLocale = parseLocale(code)

    init {
        require(locale.language.isNotBlank())
    }
}
