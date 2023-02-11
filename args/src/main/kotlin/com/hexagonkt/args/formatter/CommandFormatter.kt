package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command
import com.hexagonkt.args.Formatter
import com.hexagonkt.args.Property

/**
 * [summary] name, title and description
 * [definition] Usage
 * [detail] Commands, options and parameters
 *
 * @property propertyFormatter
 */
data class CommandFormatter(
    val propertyFormatter: Formatter<Property<*>> = PropertyFormatter()
) : Formatter<Command> {

    override fun summary(component: Command): String {
        val title = listOfNotNull(
            component.name,
            component.title?.let { "- $it" },
        )
        .joinToString(" ")

        return "$title\n\n${component.description}"
    }

    override fun definition(component: Command): String {
        val properties = component.options + component.parameters
        val options = properties.joinToString(" ") { propertyFormatter.summary(it) }

        return "${component.name} $options"
    }

    override fun detail(component: Command): String {
        val commands = component.subcommands.dl("\n", "  ") { it.name to (it.title ?: "") }
        val options = component.options.dl(" ", "  ")
        val parameters = component.parameters.dl(" ", "  ")

        return "Commands:\n$commands\n\nParameters:\n$parameters\n\nOptions:\n$options"
    }

    private fun <T : Any> Collection<T>.dl(
        separator: String, indent: String, block: (T) -> Pair<String, String>
    ) : String =
        map(block)
            .let { p ->
                val m = p.maxOf { it.first.length }
                p.map { (k, v) -> k.padEnd(m + 3, ' ') + v }
            }
            .joinToString(separator) { it.trim().prependIndent(indent) }

    private fun Collection<Property<*>>.dl(separator: String, indent: String) : String =
        dl(separator, indent) { propertyFormatter.definition(it) to propertyFormatter.detail(it) }
}
