package com.hexagonkt.models

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
