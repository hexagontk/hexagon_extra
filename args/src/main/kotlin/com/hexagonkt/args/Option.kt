package com.hexagonkt.args

import java.net.InetAddress
import java.net.URI
import java.net.URL
import kotlin.reflect.KClass

data class Option<T : Any>(
    val type: KClass<T>,
    val shortName: Char? = null,
    val longName: String? = null,
    val description: String? = null,
    val optional: Boolean = true,
    val list: Boolean = false,
    val defaultValue: T? = null,
) {
    internal companion object {
        val allowedTargetTypes: Set<KClass<*>> = setOf(
            Boolean::class,
            Int::class,
            Long::class,
            Float::class,
            Double::class,
            String::class,
            InetAddress::class,
            URL::class,
            URI::class,
        )
    }

    init {
        require(type in allowedTargetTypes) {
            "Type $type not in allowed types: $allowedTargetTypes"
        }
        require(shortName?.isLetterOrDigit() ?: true) {
            "Short name must be a letter or a digit: $shortName"
        }
        require((longName?.trim()?.length ?: 2) > 1) {
            "Long name must be at least two characters: $longName"
        }
        require(description?.isNotBlank() ?: true) {
            "Description cannot be blank"
        }
    }
}
