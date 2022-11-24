package com.hexagonkt.dokka.json

import com.hexagonkt.core.println
import com.hexagonkt.core.requireKeys
import com.hexagonkt.serialization.SerializationManager
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.parseMap
import com.hexagonkt.serialization.serialize
import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JsonPluginTest : BaseAbstractTest() {

    private val configuration = dokkaConfiguration {
        moduleName = "mod"

        sourceSets {
            sourceSet {
                sourceRoots = listOf("src/main/kotlin")
            }
        }
    }

    @Test fun `JsonPlugin serialize documentation to a JSON file`() {
        testInline(
            """
            |/src/main/kotlin/sample/Test.kt
            |package sample
            |/**
            | * Data class description. Detailed information.
            | *
            | * @property reason Class property.
            | */
            |data class TestingIsEasy(val reason: String) {
            |  /**
            |   * Method definition.
            |   *
            |   * @param parameter Method parameter.
            |   * @return The return description.
            |   */
            |  fun docFun(parameter: String): Int = 1
            |}
            """.trimIndent(), configuration
        ) {
            documentablesTransformationStage = { module ->
                val sample = module.packages.find { it.name == "sample" }
                assertNotNull(sample)
                assertNotNull(sample.classlikes.find { it.name == "TestingIsEasy" })
            }
        }

        SerializationManager.defaultFormat = Json
        val json = JsonPlugin.outputDirectory.resolve("module_mod.json").parseMap()
        json.serialize(Json).println("JSON>\n")
        assertEquals("mod", json.requireKeys("name"))
    }
}
