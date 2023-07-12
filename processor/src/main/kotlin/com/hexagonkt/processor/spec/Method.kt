package com.hexagonkt.processor.spec

import com.hexagonkt.core.filterNotEmptyRecursive
import com.hexagonkt.core.mapOfNotNull

data class Method(
    val method: String,
    val description: String,
) {
    fun toMap(): Map<*, *> =
        mapOf(
            method to mapOfNotNull(
                "description" to description,
                "responses" to mapOf(
                    "200" to mapOf(
                        "content" to mapOf(
                            "application/json" to mapOf(
                                "com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("type" to "object")
                            ),
                            "application/yaml" to mapOf(
                                "com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("type" to "object")
                            ),
                        ),
                    ),
                ),
            )
        )
        .filterNotEmptyRecursive()
}
