package com.hexagonkt.dokka.json

import com.hexagonkt.core.filterNotEmptyRecursive
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.serialize
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.model.*
import org.jetbrains.dokka.model.doc.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement
import org.jetbrains.dokka.transformers.documentation.DocumentableTransformer
import java.io.File

typealias MapPair = Pair<String, Map<String, Map<String, *>>>

/**
 * JSON Dokka plugin implementation. This plugin is a document transformer which create a file
 * without altering the documentation model.
 */
class JsonPlugin : DokkaPlugin() {

    internal val extension by extending {
        CoreExtensions.documentableTransformer providing {
            DocumentableTransformer(::processModule)
        }
    }

    private fun processModule(module: DModule, context: DokkaContext): DModule =
        module.apply {
            val name = module.name
            val documentation = processDocumentation(module.documentation)
            val packages = module.packages
                .associateBy { it.name }
                .mapValues { processPackage(it.value) }

            val configurationOutput = context.configuration.outputDir
            val outputDirectory =
                if (configurationOutput.absolutePath.startsWith("/tmp")) File("build/dokka")
                else configurationOutput
            if (!outputDirectory.exists())
                outputDirectory.mkdirs()
            val file = outputDirectory.resolve("module_$name.json")
            val map = mapOf(
                "name" to name,
                "documentation" to documentation,
                "packages" to packages
            )

            file.writeText(map.filterNotEmptyRecursive().serialize(Json))
        }

    private fun processPackage(pack: DPackage): Map<String, *> {
        val documentation = processDocumentation(pack.documentation)
        return mapOf(
            "documentation" to documentation,
            "properties" to pack.properties.associate { processDocumentable(it) },
            "functions" to pack.functions.associate { processFunction(it) },
            "types" to pack.classlikes.associate { processType(it) },
        )
    }

    private fun processFunction(function: DFunction): MapPair =
        function.name to mapOf(
            "documentation" to processDocumentation(function.documentation),
            "parameters" to function.parameters.associate { processDocumentable(it) },
        )

    private fun processType(type: DClasslike): MapPair =
        (type.name ?: error("Type must have a name")) to mapOf(
            "documentation" to processDocumentation(type.documentation),
            "properties" to type.properties.associate { processDocumentable(it) },
            "types" to type.classlikes.associate { processType(it) },
            "functions" to type.functions.associate { processFunction(it) },
        )

    private fun processDocumentable(documentable: Documentable): MapPair =
        (documentable.name ?: error("Documentable must have a name")) to
            mapOf("documentation" to processDocumentation(documentable.documentation) { true })

    private fun processDocumentation(
        documentation: SourceSetDependent<DocumentationNode>,
        filter: (TagWrapper) -> Boolean = { it is Description },
    ): Map<String, String> =
        documentation
            .mapKeys { it.key.displayName }
            .mapValues { renderText(it.value.children.filter(filter)) }

    private fun renderText(documentation: List<TagWrapper>): String =
        documentation
            .flatMap { it.children }
            .joinToString("\n") {
                val text = it.children.joinToString("") { node ->
                    when (node) {
                        is Text -> node.body
                        is DocumentationLink -> joinBodies(node)
                        is CodeInline -> joinBodies(node).let { x -> "`$x`" }
                        else -> joinBodies(node)
                    }
                }
                when (it) {
                    is P -> "\n$text"
                    is H1 -> "# $text"
                    is H2 -> "## $text"
                    is H3 -> "### $text"
                    is H4 -> "#### $text"
                    is H5 -> "##### $text"
                    is H6 -> "###### $text"
                    else -> text
                }
            }
            .trim()

    private fun joinBodies(c: DocTag) =
        c.childrenOfType<Text>().joinToString(transform = Text::body)

    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement =
        PluginApiPreviewAcknowledgement
}
