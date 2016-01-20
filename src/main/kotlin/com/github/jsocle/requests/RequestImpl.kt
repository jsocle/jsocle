package com.github.jsocle.requests

import com.github.jsocle.JSocle
import com.github.jsocle.JSocleApp
import com.github.jsocle.requests.session.Session
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import kotlin.collections.firstOrNull
import kotlin.collections.map
import kotlin.collections.mapOf
import kotlin.collections.toMap

public class RequestImpl(override val url: String, override val pathVariables: Map<String, Any>,
                         override val servlet: HttpServletRequest, override val method: Request.Method,
                         override val handler: RequestHandler<*>, override val handlerCallStack: Array<JSocleApp>,
                         app: JSocle) : Request {
    override val cookies: Array<Cookie>
        get() = servlet.cookies ?: arrayOf()

    override val g = Request.RequestGlobal()

    public override val session: Session by lazy(LazyThreadSafetyMode.NONE) { ->
        app.buildSession(servlet.cookies?.firstOrNull { it.name == "session" }?.value)
    }

    override fun parameter(name: String): String? {
        return parameters[name]?.firstOrNull()
    }

    override val parameters: Map<String, List<String>> by lazy(LazyThreadSafetyMode.NONE) {
        (servlet.parameterMap ?: mapOf<String, Array<String>>())
                .map { it.key to it.value.map { it.toString() } }
                .toMap()
    }
}