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
        component.let { c ->
            listOfNotNull(
                c.description?.let { if (it.endsWith('.')) it else "$it." },
                (c.regex?.pattern ?: c.typeName())?.let { c.format(it) },
                c.values
                    .ifEmpty { null }
                    ?.map(Any::toString)
                    ?.let { "Default: " + if (c.multiple) c.values else c.value },
            )
        }
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
