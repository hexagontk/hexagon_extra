package com.hexagonkt.models

import java.util.*

/**
 * I18N String, glorified map.
 */
class Text(
    /** Map with locale language code as key and text as value. */
    private val translations: Map<Locale, String>,
) : Map<Locale, String> by translations {

    val text: String by lazy { translations.entries.first().value }

    constructor(value: String) : this(mapOf(Locale.ENGLISH to value))
    constructor(vararg translations: Pair<String, String>) :
        this(translations.associate { (k, v) -> Locale.forLanguageTag(k) to v })

    init {
        require(translations.isNotEmpty()) { "There must be at least one translation" }
        translations.entries.forEach {
            require(it.value.isNotBlank()) { "Blank translation value" }
        }
    }

    fun getOrDefault(key: String): String =
        getOrDefault(Locale.forLanguageTag(key))

    fun getOrDefault(key: Locale): String =
        translations[key] ?: text

    operator fun get(key: String): String? =
        translations[Locale.forLanguageTag(key)]

    override fun equals(other: Any?): Boolean =
        translations == other

    override fun hashCode(): Int =
        translations.hashCode()

    override fun toString(): String =
        translations.toString()
}
