package com.hexagonkt.http.test

import com.hexagonkt.core.media.APPLICATION_JSON
import com.hexagonkt.http.SslSettings
import com.hexagonkt.http.client.HttpClient
import com.hexagonkt.http.client.HttpClientPort
import com.hexagonkt.http.client.HttpClientSettings
import com.hexagonkt.http.model.HttpRequest
import com.hexagonkt.http.model.*
import com.hexagonkt.http.model.HttpMethod.*
import java.net.URL

data class Http(
    val adapter: HttpClientPort,
    val url: String? = null,
    val contentType: ContentType? = ContentType(APPLICATION_JSON),
    val headers: Map<String, *> = emptyMap<String, Any>(),
    val sslSettings: SslSettings? = SslSettings(),
) {

    private val settings =
        HttpClientSettings(
            baseUrl = url?.let(::URL),
            contentType = contentType,
            useCookies = true,
            headers = toHeaders(headers),
            insecure = true,
            sslSettings = sslSettings,
        )

    private val http = HttpClient(adapter, settings)

    lateinit var response: HttpResponsePort

    fun request(block: Http.() -> Unit) {
        http.request { block.invoke(this@Http) }
    }

    private fun toHeaders(map: Map<String, *>): Headers = Headers(
        map.mapValues { (k, v) ->
            Header(
                k,
                when (v) {
                    is Collection<*> -> v.map { it.toString() }.toList()
                    else -> listOf(v.toString())
                }
            )
        }
    )

    private fun send(
        method: HttpMethod = GET,
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        http
            .apply {
                if (!started())
                    start()
            }
            .send(
                HttpRequest(
                    method = method,
                    path = path,
                    body = body,
                    headers = toHeaders(headers),
                    formParameters = FormParameters(formParameters),
                    parts = parts,
                    contentType = contentType,
                )
            )
            .apply { response = this }

    fun get(
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        send(GET, path, headers, body, formParameters, parts, contentType)

    fun put(
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        send(PUT, path, headers, body, formParameters, parts, contentType)

    fun post(
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        send(POST, path, headers, body, formParameters, parts, contentType)

    fun options(
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        send(OPTIONS, path, headers, body, formParameters, parts, contentType)

    fun delete(
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        send(DELETE, path, headers, body, formParameters, parts, contentType)

    fun patch(
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        send(PATCH, path, headers, body, formParameters, parts, contentType)

    fun trace(
        path: String = "/",
        headers: Map<String, *> = emptyMap<String, Any>(),
        body: Any = "",
        formParameters: List<FormParameter> = emptyList(),
        parts: List<HttpPart> = emptyList(),
        contentType: ContentType? = this.contentType,
    ): HttpResponsePort =
        send(TRACE, path, headers, body, formParameters, parts, contentType)
}
