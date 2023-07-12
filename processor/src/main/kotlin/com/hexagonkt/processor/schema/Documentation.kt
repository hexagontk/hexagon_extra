package com.hexagonkt.processor.schema

import com.hexagonkt.core.getPath

// TODO Maybe this fits better on the Dokka JSON plugin
data class Documentation(val dokkaData: Map<String, *>) {

    private val packages: Map<String, Map<String, *>> = dokkaData.getPath("packages") ?: emptyMap()
    private val types: Map<String, Map<String, *>> = packages.flatKey("types")
    private val properties: Map<String, Map<String, *>> = types.flatKey("properties")

    private val documentation: Map<String, String> =
        (packages + types + properties)
            .mapValues { (_, v) -> v.getPath<String>("documentation", "JVM") }
            .filter { (_, v) -> v != null }
            .mapValues { (_, v) -> v as String}

    fun title(element: String): String? =
        description(element)?.substringBefore(".")?.trim()?.removeSuffix(".")

    fun description(element: String): String? =
        documentation[element]?.trim()?.removeSuffix(".")

    private fun Map<String, Map<String, *>>.flatKey(key: String): Map<String, Map<String, *>> =
        map { (k, v) -> k to v.getPath<Map<String, Map<String, *>>>(key) }
            .flatMap { (k, v) -> v?.map { (k1, v1) -> "$k.$k1" to v1  } ?: emptyList() }
            .filter { (_, v) -> v.isNotEmpty() }
            .toMap()
}
