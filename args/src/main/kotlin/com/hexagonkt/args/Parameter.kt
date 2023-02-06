package com.hexagonkt.args

import com.hexagonkt.core.camelToWords
import com.hexagonkt.core.parsedClasses
import com.hexagonkt.core.requireNotBlank
import com.hexagonkt.core.wordsToSnake
import kotlin.reflect.KClass

data class Parameter<T : Any>(
    override val type: KClass<T>,
    override val name: String? = null,
    override val description: String? = null,
    override val regex: Regex? = null,
    override val optional: Boolean = true,
    override val multiple: Boolean = false,
    override val values: List<T> = emptyList(),
) : Property<T> {

    override val value: T? = values.firstOrNull()

    val typeName: String? = type.simpleName?.camelToWords()?.wordsToSnake()?.uppercase()
    val hasValue: Boolean = type != Boolean::class

    companion object {
        val parameterRegex = "[a-z0-9\\-]{2,}".toRegex()
    }

    constructor(
        type: KClass<T>,
        name: String? = null,
        description: String? = null,
        regex: Regex? = null,
        optional: Boolean = true,
        value: T,
    ) : this(type, name, description, regex, optional, false, listOf(value))

    init {
        require(type in parsedClasses) {
            val types = parsedClasses.map(KClass<*>::simpleName)
            "Type ${type.simpleName} not in allowed types: $types"
        }
        require(name?.matches(parameterRegex) ?: true) {
            "Name must comply with ${parameterRegex.pattern} regex: $name"
        }
        require(regex == null || type == String::class) {
            "Regex can only be used for 'string' parameters: ${type.simpleName}"
        }

        requireNotBlank(Parameter<*>::description)

        if (regex != null)
            values.forEach {
                require(regex.matches(it as String)) { "Value should match the '${regex.pattern}' regex: $it" }
            }

        if (!multiple)
            require(values.size <= 1) { "Single parameter can only have one value: $values" }
    }

    override fun summary(): String =
        format(definition())

    override fun definition(): String =
        "<$name>"

    override fun detail(): String =
        listOfNotNull(
            description?.let { if (it.endsWith('.')) it else "$it." },
            (regex?.pattern ?: typeName)?.let(::format),
            values.ifEmpty { null }?.map(Any::toString)?.let { "Default: " + if (multiple) values else value },
        )
        .joinToString(" ")
}
