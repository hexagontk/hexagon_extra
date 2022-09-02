package com.hexagonkt.models

/**
 * Postal address.
 *
 * @property subregion Administrative region subdivision.
 * @property region First level administrative region.
 */
data class Address(
    val postalCode: String,
    val countryIso: String,
    val addressLine: String? = null,
    val locality: String? = null,
    val subregion: String? = null,
    val region: String? = null,
) {

    init {
        require(postalCode.isNotBlank()) { "'postalCode' cannot be blank" }
        require(countryIso.isNotBlank()) { "'countryIso' cannot be blank" }
        require(addressLine?.isNotBlank() ?: true) { "'addressLine' cannot be blank" }
        require(locality?.isNotBlank() ?: true) { "'locality' cannot be blank" }
        require(subregion?.isNotBlank() ?: true) { "'subregion' cannot be blank" }
        require(region?.isNotBlank() ?: true) { "'region' cannot be blank" }
    }
}
