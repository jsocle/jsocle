package com.github.jsocle.requests

import com.github.jsocle.JSocle
import com.github.jsocle.requests.session.Session
import javax.servlet.http.HttpServletRequest
import kotlin.properties.Delegates

public class RequestImpl(public override val url: String,
                         public override val pathVariables: Map<String, Any>,
                         public override val servlet: HttpServletRequest,
                         public override val method: Request.Method, val app: JSocle) : Request {

    public override val session: Session by Delegates.lazy { ->
        app.buildSession(servlet.getCookies()?.firstOrNull { it.getName() == "session" }?.getValue())
    }

    override fun parameter(name: String): String? {
        return parameters[name]?.firstOrNull()
    }

    override val parameters: Map<String, List<String>> by Delegates.lazy {
        (servlet.getParameterMap() ?: mapOf<String, Array<String>>())
                .map { it.getKey() to it.getValue().map { it.toString() } }
                .toMap()
    }
}