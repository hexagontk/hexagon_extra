package com.hexagonkt.args

inline fun <reified T : Any> Option(
    shortName: Char? = null,
    name: String? = null,
    description: String? = null,
    regex: Regex? = null,
    optional: Boolean = true,
    multiple: Boolean = false,
    tag: String? = null,
) : Option<T> =
    Option(
        T::class,
        shortName,
        name,
        description,
        regex,
        optional,
        multiple,
        tag,
    )

inline fun <reified T : Any> Option(
    shortName: Char? = null,
    name: String? = null,
    description: String? = null,
    regex: Regex? = null,
    tag: String? = null,
    values: List<T>
) : Option<T> =
    Option(
        type = T::class,
        names = setOfNotNull(shortName?.toString(), name),
        description = description,
        regex = regex,
        optional = true,
        multiple = true,
        tag = tag,
        values = values
    )

inline fun <reified T : Any> Option(
    shortName: Char? = null,
    name: String? = null,
    description: String? = null,
    regex: Regex? = null,
    tag: String? = null,
    value: T
) : Option<T> =
    Option(
        type = T::class,
        names = setOfNotNull(shortName?.toString(), name),
        description = description,
        regex = regex,
        optional = true,
        multiple = false,
        tag = tag,
        values = listOf(value)
    )

inline fun <reified T : Any> Parameter(
    name: String,
    description: String? = null,
    regex: Regex? = null,
    optional: Boolean = true,
    multiple: Boolean = false,
    tag: String? = null,
) : Parameter<T> =
    Parameter(T::class, name, description, regex, optional, multiple, tag)

inline fun <reified T : Any> Parameter(
    name: String,
    description: String? = null,
    regex: Regex? = null,
    tag: String? = null,
    values: List<T>,
) : Parameter<T> =
    Parameter(
        type = T::class,
        name = name,
        description = description,
        regex = regex,
        optional = true,
        multiple = true,
        tag = tag,
        values = values
    )

inline fun <reified T : Any> Parameter(
    name: String,
    description: String? = null,
    regex: Regex? = null,
    tag: String? = null,
    value: T,
) : Parameter<T> =
    Parameter(
        type = T::class,
        name = name,
        description = description,
        regex = regex,
        optional = true,
        multiple = false,
        tag = tag,
        values = listOf(value)
    )
