package com.hexagonkt.web

import com.hexagonkt.core.media.mediaTypeOfOrNull
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.http.server.handlers.HttpServerContext
import com.hexagonkt.templates.TemplateManager
import com.hexagonkt.templates.TemplatePort
import java.net.URL
import java.nio.charset.Charset.defaultCharset
import java.util.Locale
import java.util.Locale.forLanguageTag as localeFor

fun HttpServerContext.templateType(url: URL): ContentType? =
    response.contentType ?: run {
        val mimeType = mediaTypeOfOrNull(url)
        mimeType?.let { ContentType(it, charset = defaultCharset()) }
    }

fun HttpServerContext.callContext(): Map<String, *> =
    mapOf(
        "_path_" to request.path.removeSuffix("/"), // Do not allow trailing slash
        "_lang_" to obtainLocale().language,
    )

/**
 * TODO Review order precedence and complete code (now only taking request attribute)
 *
 * 1. Request
 * 2. Session
 * 3. Cookie
 * 4. Accept-language
 * 5. Server default locale
 */
fun HttpServerContext.obtainLocale(): Locale = when {
    attributes["lang"] as? String != null -> localeFor(attributes["lang"] as String)
    else -> Locale.getDefault()
}

fun HttpServerContext.template(
    templateEngine: TemplatePort,
    url: URL,
    context: Map<String, *> = callContext(),
    locale: Locale = obtainLocale(),
): HttpServerContext =
    ok(templateEngine.render(url, context, locale), contentType = templateType(url))

fun HttpServerContext.template(
    url: URL,
    context: Map<String, *> = callContext(),
    locale: Locale = obtainLocale(),
): HttpServerContext =
    ok(TemplateManager.render(url, context, locale), contentType = templateType(url))
