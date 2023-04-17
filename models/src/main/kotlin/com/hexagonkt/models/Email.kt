package com.hexagonkt.models

import jakarta.mail.internet.InternetAddress

data class Email(val address: String) {

    init {
        try {
            InternetAddress(address).validate()
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message, e)
        }
    }

    val user: String by lazy { address.substringBeforeLast('@') }

    val domain: String by lazy { address.substringAfterLast('@') }
}
