package com.hexagonkt.tools

class Generator {

    private fun mapToMap(m: Any?): String =
        when (m) {
            is Map<*, *> ->
                m.entries.joinToString(",\n", "mapOf(\n", "\n)") { (k, v) ->
                    "\"$k\" to ${mapToMap(v)}"
                }

            is List<*> ->
                m.joinToString(",\n", "listOf(\n", "\n)", transform = ::mapToMap)

            is String -> "\"\"\"$m\"\"\""
            is Number -> m.toString()
            is Boolean, null -> m.toString()
            else -> error("Error: ${m::class.qualifiedName}")
        }
}
