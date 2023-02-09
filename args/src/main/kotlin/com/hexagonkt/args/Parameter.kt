package com.hexagonkt.args

import com.hexagonkt.core.parsedClasses
import com.hexagonkt.core.requireNotBlank
import kotlin.reflect.KClass

data class Parameter<T : Any>(
    override val type: KClass<T>,
    val name: String,
    override val description: String? = null,
    override val regex: Regex? = null,
    override val optional: Boolean = true,
    override val multiple: Boolean = false,
    override val values: List<T> = emptyList(),
) : Property<T> {

    override val names: LinkedHashSet<String> = linkedSetOf(name)

    val value: T? = values.firstOrNull()

    companion object {
        val nameRegex = "[a-z0-9\\-]{2,}".toRegex()
    }

    constructor(
        type: KClass<T>,
        name: String,
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
        require(names.isNotEmpty()) { "" }
        require(names.all { it.matches(nameRegex) }) {
            "Names must comply with ${nameRegex.pattern} regex: $names"
        }
        require(regex == null || type == String::class) {
            "Regex can only be used for 'string' parameters: ${type.simpleName}"
        }

        requireNotBlank(Parameter<*>::description)

        if (regex != null)
            values.forEach {
                require(regex.matches(it as String)) {
                    "Value should match the '${regex.pattern}' regex: $it"
                }
            }

        if (!multiple)
            require(values.size <= 1) { "Single parameter can only have one value: $values" }
    }
}
