package com.hexagonkt.dokka.json

import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.serialize
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.pages.ClasslikePageNode
import org.jetbrains.dokka.pages.MemberPageNode
import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.renderers.Renderer

class JsonPlugin : DokkaPlugin() {

    private val dokkaBase by lazy { plugin<DokkaBase>() }

    @Suppress("unused") // This field (even being not used) is required for the Dokka plugin to work
    internal val extension by extending {
        CoreExtensions.renderer providing ::renderer override dokkaBase.htmlRenderer
    }

    private fun renderer(context: DokkaContext): Renderer =
        Renderer { root -> render(context, root) }

    private fun render(context: DokkaContext, root: RootPageNode) {
        val children = root.children
            .flatMap { it.children }
            .map {
                when (it) {
                    is MemberPageNode -> "Member ${it.name} ${it.content}"
                    is ClasslikePageNode -> "Type ${it.name} ${it.content}"
                    else -> "-"
                }
            }

        val outFile = context.configuration.outputDir.resolve("dokka.json")
        val json = children.serialize(Json)

        outFile.writeText(json)
    }
}
