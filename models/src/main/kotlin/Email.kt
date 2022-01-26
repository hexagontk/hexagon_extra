package com.hexagonkt.models

import javax.mail.internet.InternetAddress

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
