package com.hexagonkt.args

import com.hexagonkt.args.Option.Companion.optionRegex
import kotlin.reflect.KClass

data class Flag(
    override val names: LinkedHashSet<String>,
    override val description: String? = null,
    override val multiple: Boolean = false,
    override val values: List<Boolean> = emptyList(),
) : Property<Boolean> {

    override val optional: Boolean = true
    override val regex: Regex? = null
    override val type: KClass<Boolean> = Boolean::class

    val value: Boolean? = values.firstOrNull()

    constructor(
        shortName: Char? = null,
        name: String? = null,
        description: String? = null,
        multiple: Boolean = false,
    ) : this(LinkedHashSet(listOfNotNull(shortName?.toString(), name)), description, multiple)

    init {
        check("Flag", optionRegex)
    }

    override fun addValue(value: String): Flag =
        copy(values = values + true)
}
