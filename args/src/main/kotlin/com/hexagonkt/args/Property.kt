package com.hexagonkt.args

import kotlin.reflect.KClass

sealed interface Property<T : Any> {
    val type: KClass<T>
    val name: String?
    val description: String?
    val regex: Regex?
    val optional: Boolean
    val multiple: Boolean
    val values: List<T>
    val value: T?

    fun summary(): String
    fun definition(): String
    fun detail(): String

    fun format(text: String): String =
        when {
            optional && multiple -> "[$text]..."
            optional -> "[$text]"
            multiple -> "$text..."
            else -> text
        }
}
