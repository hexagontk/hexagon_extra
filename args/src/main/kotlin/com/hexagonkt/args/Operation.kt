package com.hexagonkt.args

sealed interface Operation {
    val name: String
    val title: String?
    val description: String?
    val properties: LinkedHashSet<Property<*>>
    val propertiesMap: Map<String, Property<*>>
}
