package com.hexagonkt.helpers

import com.hexagonkt.core.eol
import kotlin.IllegalArgumentException
import java.text.Normalizer.Form.NFD
import java.text.Normalizer.normalize

/**
 * [TODO](https://github.com/hexagonkt/hexagon/issues/271).
 *
 * @receiver .
 * @param T .
 * @param converter .
 * @return .
 */
fun <T : Enum<*>> String.toEnum(converter: (String) -> T): T =
    uppercase().replace(" ", "_").let(converter)

/**
 * [TODO](https://github.com/hexagonkt/hexagon/issues/271).
 *
 * @receiver .
 * @param T .
 * @param converter .
 * @return .
 */
fun <T : Enum<*>> String.toEnumOrNull(converter: (String) -> T): T? =
    try {
        toEnum(converter)
    }
    catch (e: IllegalArgumentException) {
        null
    }

/**
 * [TODO](https://github.com/hexagonkt/hexagon/issues/271).
 *
 * @receiver .
 * @param text .
 * @return .
 */
fun Regex.findGroups(text: String): List<MatchGroup> =
    (this.find(text)?.groups ?: emptyList<MatchGroup>())
        .filterNotNull()
        .drop(1)

/**
 * Format the string as a banner with a delimiter above and below text. The character used to
 * render the delimiter is defined.
 *
 * @param bannerDelimiter Delimiter char for banners.
 */
fun String.banner(bannerDelimiter: String = "*"): String =
    bannerDelimiter
        .repeat(this
            .lines()
            .asSequence()
            .map { it.length }
            .maxOrElse(0)
        )
        .let { "$it$eol$this$eol$it" }

/**
 * [TODO](https://github.com/hexagonkt/hexagon/issues/271).
 *
 * @receiver .
 * @return .
 */
fun String.stripAccents(): String =
    normalize(this, NFD).replace("\\p{M}".toRegex(), "")

/**
 * [TODO](https://github.com/hexagonkt/hexagon/issues/271).
 *
 * @param bytes .
 * @return .
 */
fun utf8(vararg bytes: Int): String =
    String(bytes.map(Int::toByte).toByteArray())

fun String.toEnumValue(): String =
    trim().uppercase().replace(" ", "_")

internal fun Sequence<Int>.maxOrElse(fallback: Int): Int =
    this.maxOrNull() ?: fallback
