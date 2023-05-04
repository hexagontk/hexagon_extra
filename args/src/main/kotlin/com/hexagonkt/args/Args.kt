package com.hexagonkt.args

inline fun <reified T : Any> Option(
    names: Set<String>,
    description: String? = null,
    regex: Regex? = null,
    optional: Boolean = true,
    multiple: Boolean = false,
    tag: String? = null,
    values: List<T> = emptyList(),
): Option<T> =
    Option(T::class, names, description, regex, optional, multiple, tag, values)

inline fun <reified T : Any> Option(
    shortName: Char? = null,
    name: String? = null,
    description: String? = null,
    regex: Regex? = null,
    optional: Boolean = true,
    multiple: Boolean = false,
    tag: String? = null,
    values: List<T> = emptyList(),
) : Option<T> =
    Option(
        T::class,
        setOfNotNull(shortName?.toString(), name),
        description,
        regex,
        optional,
        multiple,
        tag,
        values
    )

inline fun <reified T : Any> Option(
    shortName: Char? = null,
    name: String? = null,
    description: String? = null,
    regex: Regex? = null,
    optional: Boolean = true,
    multiple: Boolean = false,
    tag: String? = null,
    value: T,
) : Option<T> =
    Option(T::class, shortName, name, description, regex, optional, multiple, tag, listOf(value))

inline fun <reified T : Any> Parameter(
    name: String,
    description: String? = null,
    regex: Regex? = null,
    optional: Boolean = true,
    multiple: Boolean = false,
    tag: String? = null,
    values: List<T> = emptyList(),
) : Parameter<T> =
    Parameter(T::class, name, description, regex, optional, multiple, tag, values)

inline fun <reified T : Any> Parameter(
    name: String,
    description: String? = null,
    regex: Regex? = null,
    optional: Boolean = true,
    tag: String? = null,
    value: T,
) : Parameter<T> =
    Parameter(T::class, name, description, regex, optional, false, tag, listOf(value))
