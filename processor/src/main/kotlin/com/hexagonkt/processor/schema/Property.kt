package com.hexagonkt.processor.schema

import com.hexagonkt.processor.schema.PropertyType.ARRAY
import com.hexagonkt.processor.schema.PropertyType.OBJECT

data class Property(
    val type: Set<PropertyType> = emptySet(),
    val name: String? = null,
    val enum: Set<*> = emptySet<Any>(),
    val title: String? = null,
    val description: String? = null,
    val properties: Set<Property> = emptySet(),
    val items: Property? = null,
    val uniqueItems: Boolean = false,
    val pattern: Regex? = null,
    val required: Set<String> = emptySet(),
    val ref: String? = null,
    val format: PropertyFormat? = null,
    val deprecated: Boolean = false,
    val default: Any? = null,
    val additionalProperties: Boolean = false,
) {
    private val schemaType: Any = type
        .map { it.toString().lowercase() }
        .let { if (it.size == 1) it.first() else it }

    constructor(type: Set<PropertyType>, name: String, pattern: String):
        this(type = type, name = name, pattern = Regex(pattern))

    fun toDefinitionsMap(): Map<*, *> =
        documentationMap() + when {
            setOf(ARRAY, OBJECT).any { type.contains(it) } -> mapOf(
                "type" to schemaType,
                "uniqueItems" to if (uniqueItems) true else null,
                "items" to itemsMap(),
                "required" to required,
                "additionalProperties" to if (!additionalProperties) false else null,
                "properties" to properties
                    .associateBy { it.name }
                    .mapValues { (_, v) -> v.toPropertiesMap() }
                    .ifEmpty { null },
            )
            else -> scalarMap()
        } + extraMap()

    private fun toPropertiesMap(): Map<*, *> =
        documentationMap() + coreMap() + extraMap()

    private fun coreMap() = when {
        type.contains(OBJECT) && ref == null -> mapOf("type" to schemaType)
        type.contains(ARRAY) -> mapOf(
            "type" to schemaType,
            "uniqueItems" to if (uniqueItems) true else null,
            "items" to itemsMap(),
        )

        ref != null -> mapOf("\$ref" to ref)
        else -> scalarMap()
    }

    private fun scalarMap(): Map<*, *> =
        mapOf(
            "type" to schemaType,
            "format" to format,
            "enum" to enum.ifEmpty { null },
            "pattern" to pattern?.pattern,
        )

    private fun extraMap(): Map<*, *> =
        mapOf(
            "deprecated" to if (deprecated) true else null,
            "default" to default,
        )

    private fun itemsMap(): Map<*, *>? =
        items?.coreMap()?.plus(items.extraMap())

    private fun documentationMap(): Map<*, *> =
        mapOf(
            "title" to if (title?.isNotBlank() == true) title else null,
            "description" to if (description == title) null else description,
        )
}
