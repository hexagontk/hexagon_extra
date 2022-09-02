package com.hexagonkt.models

/**
 * Phone number.
 */
data class Phone(
    val number: String,
    val countryCode: String? = null,
    val landLine: Boolean? = null,
    val description: String? = null,
) {

    init {
        require(number.isNotBlank()) { "'number' cannot be blank" }
        require(countryCode?.isNotBlank() ?: true) { "'countryCode' cannot be blank" }
    }
}
