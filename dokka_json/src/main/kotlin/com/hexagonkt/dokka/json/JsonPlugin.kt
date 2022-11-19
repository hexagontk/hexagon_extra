package com.hexagonkt.dokka.json

import com.hexagonkt.core.filterEmptyRecursive
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.serialize
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.model.*
import org.jetbrains.dokka.model.doc.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.transformers.documentation.DocumentableTransformer
import java.io.File

/**
 * Marker text. Extended text.
 */
class JsonPlugin : DokkaPlugin() {

    @Suppress("unused") // This field (even being not used) is required for the Dokka plugin to work
    internal val extension by extending {
        CoreExtensions.documentableTransformer providing {
            DocumentableTransformer { module, context ->
                processModule(module, context)
                module.packages.map { processPackage(it, context) }
                module
            }
        }
    }

    private fun processModule(module: DModule, context: DokkaContext) {
        val name = module.name
        val packages = module.packages.map { it.name }
        val documentation = renderDocumentation(module.documentation)
        val map = mapOf("name" to name, "packages" to packages, "documentation" to documentation)

        context.configuration.outputDir.resolve("module_$name.json").write(map)
    }

    private fun processPackage(pack: DPackage, context: DokkaContext) {
        val name = pack.name
        val documentation = renderDocumentation(pack.documentation)
        val map = mapOf(
            "name" to name,
            "documentation" to documentation,
            "properties" to pack.properties.associate { renderDocumentation(it) },
            "functions" to pack.functions.associate { renderDocumentation(it) },
            "types" to pack.classlikes.associate { renderDocumentation(it) },
        )

        context.configuration.outputDir.resolve("package_$name.json").write(map)
    }

    private fun renderDocumentation(
        documentation: DFunction
    ): Pair<String, Map<String, Map<String, *>>> =
        documentation.name to mapOf(
            "documentation" to renderDocumentation(documentation.documentation),
            "parameters" to documentation.parameters.associate { renderDocumentation(it) },
        )

    private fun renderDocumentation(
        documentation: DClasslike
    ): Pair<String, Map<String, Map<String, *>>> =
        (documentation.name ?: error("")) to mapOf(
            "documentation" to renderDocumentation(documentation.documentation),
            "properties" to documentation.properties.associate { renderDocumentation(it) },
            "types" to documentation.classlikes.associate { renderDocumentation(it) },
            "functions" to documentation.functions.associate { renderDocumentation(it) },
        )

    private fun renderDocumentation(
        documentation: Documentable
    ): Pair<String, Map<String, Map<String,String>>> =
        (documentation.name ?: error("Documentable must have a name")) to
            mapOf("documentation" to renderDocumentation(documentation.documentation))

    private fun renderDocumentation(
        documentation: SourceSetDependent<DocumentationNode>
    ): Map<String, String> =
        documentation
            .mapKeys { it.key.displayName }
            .mapValues { renderDocumentation(it.value) }

    private fun renderDocumentation(documentation: DocumentationNode): String =
        documentation.children
            .flatMap { it.children }
            .joinToString("\n") {
                val text = it.childrenOfType<Text>().map(Text::body).joinToString(" ")
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

    private fun File.write(data: Map<String, Any>) {
        writeText(data.filterEmptyRecursive().serialize(Json))
    }
}
