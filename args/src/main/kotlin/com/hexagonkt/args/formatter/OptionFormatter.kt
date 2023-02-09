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
        listOfNotNull(
            component.description?.let { if (it.endsWith('.')) it else "$it." },
            (component.regex?.pattern ?: component.typeName())?.let { component.format(it) },
            component.values.ifEmpty { null }?.map(Any::toString)
                ?.let { "Default: " + if (component.multiple) component.values else component.value },
        )
        .joinToString(" ")

    private fun <T : Any> Option<T>.hasValue(): Boolean =
        type != Boolean::class

    private fun <T : Any> Option<T>.aliases() =
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
