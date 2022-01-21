package com.hexagonkt.models

import java.lang.IllegalArgumentException
import java.util.*
import javax.mail.internet.InternetAddress

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

/**
 * Postal address.
 *
 * @property subregion Administrative region subdivision.
 * @property region First level administrative region.
 */
data class Address(
    val addressLine: String,
    val postalCode: String,
    val countryIso: String,
    val locality: String? = null,
    val subregion: String? = null,
    val region: String? = null,
) {

    init {
        require(addressLine.isNotBlank()) { "'addressLine' cannot be blank" }
        require(postalCode.isNotBlank()) { "'postalCode' cannot be blank" }
        require(countryIso.isNotBlank()) { "'countryIso' cannot be blank" }
        require(locality?.isNotBlank() ?: true) { "'locality' cannot be blank" }
        require(subregion?.isNotBlank() ?: true) { "'subregion' cannot be blank" }
        require(region?.isNotBlank() ?: true) { "'region' cannot be blank" }
    }
}

enum class Sex {
    FEMALE,
    MALE,
    OTHER,
}

enum class Gender {
    WOMAN,
    MAN,
    LESBIAN,
    GAY,
    TRANSGENDER_WOMAN,
    TRANSGENDER_MAN,
    BISEXUAL,
    INTERSEXUAL,
    QUEER,
    AGENDER,
}

enum class Pronoun { FEMININE, MASCULINE, NEUTRAL }

/**
 * Phone number.
 */
data class Phone(
    val number: String,
    val countryCode: String? = null,
    val landLine: Boolean? = null,
    val personal: Boolean? = null,
) {

    init {
        require(number.isNotBlank()) { "'number' cannot be blank" }
        require(countryCode?.isNotBlank() ?: true) { "'countryCode' cannot be blank" }
    }
}

/**
 * Location (coordinates in longitude/latitude).
 */
data class Location(
    val longitude: Double,
    val latitude: Double,
)

data class Email(val email: String) {

    init {
        try {
            InternetAddress(email).validate()
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message, e)
        }
    }

    val user: String by lazy { email.substringBeforeLast('@') }
}
