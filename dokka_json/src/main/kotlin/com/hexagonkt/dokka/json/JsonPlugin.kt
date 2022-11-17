package com.hexagonkt.dokka.json

import com.hexagonkt.core.println
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.serialize
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.model.DClass
import org.jetbrains.dokka.model.DModule
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.transformers.documentation.DocumentableTransformer

/**
 * Marker text. Extended text.
 */
class JsonPlugin : DokkaPlugin() {

    @Suppress("unused") // This field (even being not used) is required for the Dokka plugin to work
    internal val extension by extending {
        CoreExtensions.documentableTransformer providing {
            DocumentableTransformer { module, context ->
                process(module, context)
                module
            }
        }
    }

    private fun process(module: DModule, context: DokkaContext) {
        val children = (module.packages[0].children[0] as DClass).documentation.map { (k, v) ->
            k.displayName.println("DDDDDDDDDDD> ")
            k.println("KKKKKKKKKK>")
            v.println("VVVVVVVVVV>")
        }

        val outFile = context.configuration.outputDir.resolve("dokka.json")
        val json = children.serialize(Json)

        outFile.writeText(json)
    }
}
