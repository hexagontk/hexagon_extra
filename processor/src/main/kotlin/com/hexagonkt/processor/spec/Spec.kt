package com.hexagonkt.processor.spec

data class Spec(
    val title: String,
    val version: String,
    val description: String,
    val paths: Map<String, *>,
    val definitions: Map<String, *>,
) {
    fun createSpec(): Map<String, *> =
        mapOf(
            "openapi" to "3.1.0",
            "info" to mapOf(
                "title" to title,
                "version" to version,
                "description" to description,
            ),
            // TODO Calculate from paths
            "produces" to listOf("application/json", "application/yaml"),
            "paths" to paths,
            "components" to mapOf("schemas" to definitions)
        )
}
