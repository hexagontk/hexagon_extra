package com.hexagonkt.dokka.json

import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class JsonPluginTest : BaseAbstractTest() {

    private val configuration = dokkaConfiguration {
        sourceSets {
            sourceSet {
                sourceRoots = listOf("src/main/kotlin")
            }
        }
    }

    @Test fun `My awesome plugin should find packages and classes`() {
        testInline(
            """
            |/src/main/kotlin/sample/Test.kt
            |package sample
            |data class TestingIsEasy(val reason: String)
            """.trimIndent(), configuration
        ) {

            documentablesTransformationStage = { module ->

            }
            documentablesTransformationStage = { module ->
                val testedPackage = module.packages.find { it.name == "sample" }
                val testedClass = testedPackage?.classlikes?.find { it.name == "TestingIsEasy" }

                assertNotNull(testedPackage)
                assertNotNull(testedClass)
            }
        }
    }

    @Test fun `Plugin can store documentation in JSON format`() {
        JsonPlugin().extension
    }
}
