package com.hexagonkt.args

import com.hexagonkt.core.parseOrNull
import kotlin.reflect.KClass

data class Option<T : Any>(
    override val type: KClass<T>,
    override val names: LinkedHashSet<String>,
    override val description: String? = null,
    override val regex: Regex? = null,
    override val optional: Boolean = true,
    override val multiple: Boolean = false,
    override val values: List<T> = emptyList(),
) : Property<T> {

    val value: T? = values.firstOrNull()

    companion object {
        val optionRegex = "([A-Za-z0-9]|[a-z0-9\\-]{2,})".toRegex()
    }

    constructor(
        type: KClass<T>,
        shortName: Char? = null,
        name: String? = null,
        description: String? = null,
        regex: Regex? = null,
        optional: Boolean = true,
        multiple: Boolean = false,
        values: List<T> = emptyList(),
    ) : this(
        type,
        LinkedHashSet(listOfNotNull(shortName?.toString(), name)),
        description,
        regex,
        optional,
        multiple,
        values
    )

    constructor(
        type: KClass<T>,
        shortName: Char? = null,
        name: String? = null,
        description: String? = null,
        regex: Regex? = null,
        optional: Boolean = true,
        multiple: Boolean = false,
        value: T,
    ) : this(type, shortName, name, description, regex, optional, multiple, listOf(value))

    init {
        check("Option", optionRegex)
    }

    override fun addValue(value: String): Option<T> =
        value.parseOrNull(type)
            ?.let { copy(values = values + it) }
            ?: error("Option '${names.first()}' of type '${typeText()}' can not hold '$value'")
}
