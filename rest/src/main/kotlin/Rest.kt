package com.hexagonkt.rest

import com.hexagonkt.http.server.handlers.HttpServerContext
import com.hexagonkt.serialization.SerializationManager
import com.hexagonkt.serialization.parseList
import com.hexagonkt.serialization.parseMap

fun HttpServerContext.bodyList(): List<*> =
    request.bodyString().parseList(mediaType())

fun HttpServerContext.bodyMap(): Map<*, *> =
    request.bodyString().parseMap(mediaType())

fun HttpServerContext.mediaType() =
    request.contentType?.mediaType ?: SerializationManager.requireDefaultFormat().mediaType
