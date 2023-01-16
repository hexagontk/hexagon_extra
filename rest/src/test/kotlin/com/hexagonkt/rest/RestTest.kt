package com.hexagonkt.rest

import com.hexagonkt.core.media.ApplicationMedia
import com.hexagonkt.core.media.ApplicationMedia.YAML
import com.hexagonkt.http.client.model.HttpClientResponse
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.http.server.model.HttpServerRequest
import com.hexagonkt.serialization.SerializationManager
import com.hexagonkt.serialization.jackson.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class RestTest {

    @Test fun `Media type is calculated properly`() {
        SerializationManager.defaultFormat = null
        assertFailsWith<IllegalStateException> { HttpServerRequest().mediaType() }
        assertFailsWith<IllegalStateException> { HttpClientResponse().mediaType() }
        SerializationManager.defaultFormat = Json
        assertEquals(ApplicationMedia.JSON, HttpServerRequest().mediaType())
        assertEquals(ApplicationMedia.JSON, HttpClientResponse().mediaType())
        HttpServerRequest(contentType = ContentType(YAML)).apply {
            assertEquals(YAML, mediaType())
        }
        HttpClientResponse(contentType = ContentType(YAML)).apply {
            assertEquals(YAML, mediaType())
        }
    }

    @Test fun `Body is parsed to list`() {
        SerializationManager.defaultFormat = Json
        HttpServerRequest(body = """[ "a", "b", "c" ]""").apply {
            assertEquals(ApplicationMedia.JSON, mediaType())
            assertEquals(listOf("a", "b", "c"), bodyList())
        }
        HttpClientResponse(body = """[ "a", "b", "c" ]""").apply {
            assertEquals(ApplicationMedia.JSON, mediaType())
            assertEquals(listOf("a", "b", "c"), bodyList())
        }
    }

    @Test fun `Body is parsed to map`() {
        SerializationManager.defaultFormat = Json
        HttpServerRequest(body = """{ "a" : 0, "b" : 1, "c" : 2 }""").apply {
            assertEquals(ApplicationMedia.JSON, mediaType())
            assertEquals(mapOf("a" to 0, "b" to 1, "c" to 2), bodyMap())
        }
        HttpClientResponse(body = """{ "a" : 0, "b" : 1, "c" : 2 }""").apply {
            assertEquals(ApplicationMedia.JSON, mediaType())
            assertEquals(mapOf("a" to 0, "b" to 1, "c" to 2), bodyMap())
        }
    }
}
