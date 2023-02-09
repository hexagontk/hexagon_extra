package com.hexagonkt.args

import kotlin.reflect.KClass

sealed interface Property<T : Any> {
    val type: KClass<T>
    val names: LinkedHashSet<String>
    val description: String?
    val regex: Regex?
    val optional: Boolean
    val multiple: Boolean
    val values: List<T>
}
