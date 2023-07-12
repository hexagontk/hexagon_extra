package com.hexagonkt.processor.schema

import com.hexagonkt.core.filterNotEmptyRecursive
import java.net.URI

data class Schema(
    val id: URI,
    val property: Property,
    val schema: URI = URI("https://json-schema.org/draft-07/schema"),
    val defs: Set<Property> = emptySet(),
    val definitions: String = "\$defs",
) {
    private val allRefs: Set<String> =
        defs
            .asSequence()
            .map { p -> p.properties.map { it.ref ?: it.items?.ref } }
            .flatten()
            .let { it + property.ref }
            .filterNotNull()
            .map { it.removePrefix("#/$definitions/") }
            .toSet()

    fun properties(): Set<Property> =
        defs.flatMap { (it.properties + it.items).filterNotNull() }.toSet()

    fun definitionsMap(): Map<*, *> = defs
        .filter { it.name in allRefs }
        .associateBy { it.name }
        .mapValues { it.value.toDefinitionsMap() }
        .filterNotEmptyRecursive()

    fun toMap(): Map<*, *> =
        mapOf(
            "\$schema" to schema.toString(),
            "\$id" to id.toString(),
            "\$ref" to property.ref,
            definitions to definitionsMap(),
        )
}
