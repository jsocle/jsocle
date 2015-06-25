package com.github.jsocle.requests

import com.github.jsocle.requests.session.Session
import com.github.jsocle.requests.session.StringSession
import javax.servlet.http.HttpServletRequest
import kotlin.properties.Delegates

public class RequestImpl(public override val url: String,
                         public override val pathVariables: Map<String, Any>,
                         private val httpServletRequest: HttpServletRequest,
                         public override val method: Request.Method) : Request {
    public override val session: Session by Delegates.lazy { ->
        StringSession(httpServletRequest.getCookies()?.firstOrNull { it.getName() == "session" })
    }

    override fun parameter(name: String): String? {
        return parameters[name]?.firstOrNull()
    }

    override val parameters: Map<String, List<String>> by Delegates.lazy {
        (httpServletRequest.getParameterMap() ?: mapOf<String, Array<String>>())
                .map { it.getKey() to it.getValue().map { it.toString() } }
                .toMap()
    }
}