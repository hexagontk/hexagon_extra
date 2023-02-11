package com.hexagonkt.args

import com.hexagonkt.core.parseOrNull

object OptionParser {

    private const val LONG_NAME = "--[a-z]+-?[a-z]+=?[a-zA-Z\\d.]*"
    private const val SHORT_NAME = "-[a-z]+"

    private val PARAMETER_REGEX = Regex("$LONG_NAME|$SHORT_NAME")

    fun parse(options: List<Option<*>>, args: Array<String>): Map<Option<*>, *> {

        val result = mutableMapOf<Option<*>, Any>()

        for (arg in args) {
            if (!arg.startsWith("-")) continue

            if (!PARAMETER_REGEX.matches(arg)) error("InvalidOptionSyntaxException")

            val isLong = arg.startsWith("--")
            val optionWithoutPrefixedDashes = arg.dropWhile { it == '-' }

            if (isLong) {
                val split = optionWithoutPrefixedDashes.split("=")
                val option = options.find { split.first() in it.names }
                    ?: error("InvalidOptionException")

                result[option] = when {
                    split.size > 1 -> split[1].parseOrNull(option.type) ?: error("Null not allowed")
                    option.type == Boolean::class -> true
                    else -> error("OptionNeedsAValueException")
                }
            } else {
                optionWithoutPrefixedDashes.forEach { shortName ->
                    val option = options.find { shortName.toString() in it.names }
                        ?: error("InvalidOptionSyntaxException")
                    result[option] = true
                }
            }
        }

        return result
    }
}
