package com.hexagonkt.dokka.json

import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.jupiter.api.Test

class JsonPluginTest : BaseAbstractTest() {

    @Test fun `Plugin can store documentation in JSON format`() {
        JsonPlugin().extension
    }
}
