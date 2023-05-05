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
    defaultValues: List<T>
) : Option<T> =
    Option(
        type = T::class,
        names = setOfNotNull(shortName?.toString(), name),
        description = description,
        regex = regex,
        optional = true,
        multiple = true,
        tag = tag,
        values = emptyList(),
        defaultValues = defaultValues
    )

inline fun <reified T : Any> Option(
    shortName: Char? = null,
    name: String? = null,
    description: String? = null,
    regex: Regex? = null,
    tag: String? = null,
    defaultValue: T
) : Option<T> =
    Option(
        type = T::class,
        names = setOfNotNull(shortName?.toString(), name),
        description = description,
        regex = regex,
        optional = true,
        multiple = false,
        tag = tag,
        values = emptyList(),
        defaultValues = listOf(defaultValue)
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
    defaultValues: List<T>,
) : Parameter<T> =
    Parameter(
        type = T::class,
        name = name,
        description = description,
        regex = regex,
        optional = true,
        multiple = true,
        tag = tag,
        values = emptyList(),
        defaultValues = defaultValues
    )

inline fun <reified T : Any> Parameter(
    name: String,
    description: String? = null,
    regex: Regex? = null,
    tag: String? = null,
    defaultValue: T,
) : Parameter<T> =
    Parameter(
        type = T::class,
        name = name,
        description = description,
        regex = regex,
        optional = true,
        multiple = false,
        tag = tag,
        values = emptyList(),
        defaultValues = listOf(defaultValue)
    )
