package com.hexagonkt.processor.spec

import com.hexagonkt.core.filterNotEmptyRecursive

data class Path(
    val pathPattern: String,
    val description: String? = null,
    val pathParameters: Map<String, *> = emptyMap<String, Any>(),
    val methods: List<Method> = emptyList()
) {
    fun toMap(): Map<*, *> =
        (mapOf(
            pathPattern to mapOf(
                "description" to description,
                "parameters" to pathParameters,
            ),
        ) + methods.associateBy { it.method })
        .filterNotEmptyRecursive()
}
