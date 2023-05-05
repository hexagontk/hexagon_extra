package com.hexagonkt.args

import com.hexagonkt.core.parseOrNull
import kotlin.reflect.KClass

data class Option<T : Any>(
    override val type: KClass<T>,
    override val names: Set<String>,
    override val description: String? = null,
    override val regex: Regex? = null,
    override val optional: Boolean = true,
    override val multiple: Boolean = false,
    override val tag: String? = null,
    override val values: List<T> = emptyList(),
    override val defaultValues: List<T> = emptyList(),
) : Property<T> {

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
        tag: String? = null,
    ) : this(
        type,
        setOfNotNull(shortName?.toString(), name),
        description,
        regex,
        optional,
        multiple,
        tag,
    )

    constructor(
        type: KClass<T>,
        shortName: Char? = null,
        name: String? = null,
        description: String? = null,
        regex: Regex? = null,
        tag: String? = null,
        defaultValues: List<T>,
    ) : this(
        type,
        setOfNotNull(shortName?.toString(), name),
        description,
        regex,
        true,
        true,
        tag,
        emptyList(),
        defaultValues
    )

    constructor(
        type: KClass<T>,
        shortName: Char? = null,
        name: String? = null,
        description: String? = null,
        regex: Regex? = null,
        tag: String? = null,
        defaultValue: T,
    ) : this(
        type,
        setOfNotNull(shortName?.toString(), name),
        description,
        regex,
        true,
        false,
        tag,
        emptyList(),
        listOf(defaultValue)
    )

    init {
        check("Option", optionRegex)
    }

    @Suppress("UNCHECKED_CAST") // Types checked at runtime
    override fun addValues(value: Collection<*>): Property<T> =
        copy(values = values + value as List<T>)

    override fun addValue(value: String): Option<T> =
        value.parseOrNull(type)
            ?.let { copy(values = values + it) }
            ?: error("Option '${names.first()}' of type '${typeText()}' can not hold '$value'")
}
