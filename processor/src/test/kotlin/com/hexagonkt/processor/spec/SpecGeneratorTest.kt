package com.hexagonkt.processor.spec

import com.hexagonkt.processor.schema.SchemaGeneratorTest
import com.hexagonkt.core.require
import com.hexagonkt.core.urlOf
import com.hexagonkt.serialization.parseMap
import com.hexagonkt.serialization.serialize
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File

@TestInstance(PER_CLASS)
internal class SpecGeneratorTest {

    @BeforeAll fun setUp() {
        SchemaGeneratorTest.createSchema()
    }

    @Suppress("UNCHECKED_CAST") // Ignored for the sake of testing
    @Test fun `Create spec`() {
        val description = """
            CVs for programmers.


            This is a reference tool for processing 'codecv' documents. You can check this tool
            endpoints on the left menu.


            These are some useful shortcuts:


            * View the **[Served CV rendered](/cv)**

            * Get the served CV document **[YAML](/cv.yml)**, **[JSON](/cv.json)**

            * Read the **[CodeCV schemas documentation](/#cmp--schemas-cv)**

            * Check the JSON Schema spec **[YAML](/schema.yml)**, **[JSON](/schema.json)**
        """.trimIndent()

        val pathParameters = listOf(
            mapOf(
                "name" to "format",
                "in" to "path",
                "com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("type" to "string"),
                "description" to "Extension to format the source (media type)",
                "required" to true,
            )
        )

        val paths = mapOf(
            "/cv" to mapOf(
                "get" to mapOf(
                    "description" to "View the CV rendered",
                    "responses" to mapOf(
                        "200" to mapOf(
                            "content" to mapOf(
                                "text/html" to mapOf("com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("type" to "string"))
                            ),
                        ),
                    ),
                )
            ),
            "/cv.{format}" to mapOf(
                "parameters" to pathParameters,
                "get" to mapOf(
                    "description" to "View the served CV document",
                    "responses" to mapOf(
                        "200" to mapOf(
                            "content" to mapOf(
                                "application/json" to mapOf(
                                    "com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("\$ref" to "#/components/schemas/Cv")
                                ),
                                "application/yaml" to mapOf(
                                    "com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("\$ref" to "#/components/schemas/Cv")
                                ),
                            ),
                        ),
                    ),
                )
            ),
            "/schema.{format}" to mapOf(
                "parameters" to pathParameters,
                "get" to mapOf(
                    "description" to "View the CodeCV JSON Schema",
                    "responses" to mapOf(
                        "200" to mapOf(
                            "content" to mapOf(
                                "application/json" to mapOf(
                                    "com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("type" to "object")
                                ),
                                "application/yaml" to mapOf(
                                    "com/hexagonkt/processor/schemaxagonkt/processor/schema" to mapOf("type" to "object")
                                ),
                            ),
                        ),
                    ),
                )
            ),
        )
        val schemaMap = urlOf("classpath:cv.schema.yml").parseMap()

        val definitions = schemaMap.require("\$defs") as Map<String, *>
        val generator = SpecGenerator("codecv", "1.0.0", description, paths, definitions)
        val spec = generator.createSpec()

        val dir = "build/classes/kotlin/main"
        File("$dir/openapi").mkdirs()
        File("$dir/openapi/spec.yml").writeText(spec.serialize())
    }
}
