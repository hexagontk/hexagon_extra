package com.hexagonkt.args

import kotlin.reflect.KClass

data class Option<T : Any>(
    val parameter: Parameter<T>,
    val shortName: Char? = null,
) : Property<T> by parameter {

    private val typeName: String? = if (parameter.regex != null) "REGEX" else parameter.typeName

    constructor(
        type: KClass<T>,
        shortName: Char? = null,
        name: String? = null,
        description: String? = null,
        regex: Regex? = null,
        optional: Boolean = true,
        multiple: Boolean = false,
        values: List<T> = emptyList(),
    ) : this(Parameter(type, name, description, regex, optional, multiple, values), shortName)

    constructor(
        type: KClass<T>,
        shortName: Char? = null,
        name: String? = null,
        description: String? = null,
        regex: Regex? = null,
        optional: Boolean = true,
        multiple: Boolean = false,
        value: T,
    ) : this(Parameter(type, name, description, regex, optional, multiple, listOf(value)), shortName)

    init {
        require(shortName?.isLetterOrDigit() ?: true) {
            "Short name must be a letter or a digit: $shortName"
        }
        require(shortName != null || name != null) {
            "Option requires a short name, a long name or both. Short: $shortName, Long: $name"
        }
    }

    fun copy(
        type: KClass<T> = this.type,
        name: String? = this.name,
        description: String? = this.description,
        regex: Regex? = this.regex,
        optional: Boolean = this.optional,
        multiple: Boolean = this.multiple,
        values: List<T> = this.values,
        shortName: Char? = this.shortName,
    ): Option<T> =
        copy(
            parameter = parameter.copy(
                type = type,
                name = name,
                description = description,
                regex = regex,
                optional = optional,
                multiple = multiple,
                values = values,
            ),
            shortName = shortName
        )

    fun keys(): List<String> =
        listOfNotNull(shortName?.toString(), name)

    override fun summary(): String =
        format(
            aliases()
                .map { if (parameter.hasValue) "$it $typeName" else it }
                .first()
        )

    override fun definition(): String =
        aliases().joinToString(", ").let { if (parameter.hasValue) "$it $typeName" else it }

    private fun aliases() = listOfNotNull(
        shortName?.let { "-$it" },
        name?.let { "--$it" }
    )

    override fun detail(): String =
        listOfNotNull(
            description?.let { if (it.endsWith('.')) it else "$it." },
            (regex?.pattern ?: parameter.typeName)?.let(::format),
            values.ifEmpty { null }?.map(Any::toString)?.let { "Default: " + if (multiple) values else value },
        )
        .joinToString(" ")
}
