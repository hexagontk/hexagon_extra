package com.hexagonkt.rest

import com.hexagonkt.core.media.ApplicationMedia
import com.hexagonkt.core.media.ApplicationMedia.YAML
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.http.server.handlers.HttpServerContext
import com.hexagonkt.http.server.model.HttpServerRequest
import com.hexagonkt.serialization.SerializationManager
import com.hexagonkt.serialization.jackson.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class RestTest {

    @Test fun `Media type is calculated properly`() {
        SerializationManager.defaultFormat = null
        assertFailsWith<IllegalStateException> { HttpServerContext().mediaType() }
        SerializationManager.defaultFormat = Json
        assertEquals(ApplicationMedia.JSON, HttpServerContext().mediaType())
        val request = HttpServerRequest(contentType = ContentType(YAML))
        val context = HttpServerContext(request = request)
        assertEquals(YAML, context.mediaType())
    }

    @Test fun `Body is parsed to list`() {
        SerializationManager.defaultFormat = Json
        val request = HttpServerRequest(body = """[ "a", "b", "c" ]""")
        val context = HttpServerContext(request = request)
        assertEquals(ApplicationMedia.JSON, context.mediaType())
        assertEquals(listOf("a", "b", "c"), context.bodyList())
    }

    @Test fun `Body is parsed to map`() {
        SerializationManager.defaultFormat = Json
        val request = HttpServerRequest(body = """{ "a" : 0, "b" : 1, "c" : 2 }""")
        val context = HttpServerContext(request = request)
        assertEquals(ApplicationMedia.JSON, context.mediaType())
        assertEquals(mapOf("a" to 0, "b" to 1, "c" to 2), context.bodyMap())
    }
}
