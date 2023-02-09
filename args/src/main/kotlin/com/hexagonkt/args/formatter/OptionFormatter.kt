package com.hexagonkt.args.formatter

import com.hexagonkt.args.Option
import com.hexagonkt.core.camelToWords
import com.hexagonkt.core.wordsToSnake

class OptionFormatter : ComponentFormatter<Option<*>> {

    override fun summary(component: Option<*>): String =
        component.format(
            component.aliases()
                .map { if (component.hasValue()) "$it ${component.typeName()}" else it }
                .first()
        )

    override fun definition(component: Option<*>): String =
        component.aliases().joinToString(", ").let {
            if (component.hasValue()) "$it ${component.typeName()}" else it
        }

    override fun detail(component: Option<*>): String =
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

    private fun Option<*>.hasValue(): Boolean =
        type != Boolean::class

    private fun Option<*>.aliases() =
        names.map { if (it.length == 1) "-$it" else "--$it" }

    private fun Option<*>.format(text: String): String =
        when {
            optional && multiple -> "[$text]..."
            optional -> "[$text]"
            multiple -> "$text..."
            else -> text
        }

    private fun Option<*>.typeName(): String? =
        if (regex != null) "REGEX"
        else type.simpleName?.camelToWords()?.wordsToSnake()?.uppercase()
}
