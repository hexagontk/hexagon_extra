package com.hexagonkt.args.formatter

import com.hexagonkt.args.Command

class CommandFormatter : ComponentFormatter<Command> {

    override fun summary(component: Command): String =
        listOfNotNull(
            component.name,
            component.title?.let { "- $it" },
        ).joinToString(" ")

    override fun definition(component: Command): String {
        TODO("Not yet implemented")
    }

    override fun detail(component: Command): String {
        TODO("Not yet implemented")
    }
}
