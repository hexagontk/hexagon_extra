package com.hexagonkt.args.formatter

import com.hexagonkt.args.Parameter
import com.hexagonkt.core.camelToWords
import com.hexagonkt.core.wordsToSnake

class ParameterFormatter : ComponentFormatter<Parameter<*>> {

    override fun summary(component: Parameter<*>): String =
        component.format(definition(component))

    override fun definition(component: Parameter<*>): String =
        "<${component.name}>"

    override fun detail(component: Parameter<*>): String =
        listOfNotNull(
            component.description?.let { if (it.endsWith('.')) it else "$it." },
            (component.regex?.pattern ?: component.typeName())?.let { component.format(it) },
            component.values.ifEmpty { null }?.map(Any::toString)?.let { "Default: " + if (component.multiple) component.values else component.value },
        )
            .joinToString(" ")

    private fun Parameter<*>.format(text: String): String =
        when {
            optional && multiple -> "[$text]..."
            optional -> "[$text]"
            multiple -> "$text..."
            else -> text
        }

    private fun Parameter<*>.typeName(): String? =
        if (regex != null) "REGEX"
        else type.simpleName?.camelToWords()?.wordsToSnake()?.uppercase()
}
