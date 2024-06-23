package com.hexagonkt.processor.schema

import com.hexagonkt.processor.Type
import com.hexagonkt.serialization.SerializationManager
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.jackson.yaml.Yaml
import com.hexagonkt.serialization.serialize
import org.junit.jupiter.api.*
import java.io.File
import java.net.URI

internal class SchemaGeneratorTest {

    internal companion object {
        data class Demo(
            val text: String = "text",
            val option: Boolean = false,
        )

        fun createSchema() {
            SerializationManager.formats = setOf(Json, Yaml)
            val schema = SchemaGenerator(
                dataClass = Type(Demo::class),
                id = URI("https://example.com"),
                documentation = null,
                titleCaseNames = true,
                sentenceCaseEnums = true
            )

            val schemaMap = schema.toMap()
            val directory = "build/classes/kotlin/main/schemas"
            File(directory).mkdirs()
            File("$directory/cv.schema.json").writeText(schemaMap.serialize(Json))
            File("$directory/cv.schema.yml").writeText(schemaMap.serialize(Json))
        }
    }

    @Test fun `Create schema`() {
        createSchema()
    }
}
