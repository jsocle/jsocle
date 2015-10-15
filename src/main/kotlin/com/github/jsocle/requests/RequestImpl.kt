package com.github.jsocle.requests

import com.github.jsocle.JSocle
import com.github.jsocle.requests.session.Session
import javax.servlet.http.HttpServletRequest

public class RequestImpl(public override val url: String,
                         public override val pathVariables: Map<String, Any>,
                         public override val servlet: HttpServletRequest,
                         public override val method: Request.Method, val app: JSocle) : Request {

    public override val session: Session by lazy(LazyThreadSafetyMode.NONE) { ->
        app.buildSession(servlet.cookies?.firstOrNull { it.name == "session" }?.value)
    }

    override fun parameter(name: String): String? {
        return parameters[name]?.firstOrNull()
    }

    override val parameters: Map<String, List<String>> by lazy(LazyThreadSafetyMode.NONE) {
        (servlet.parameterMap ?: mapOf<String, Array<String>>())
                .map { it.getKey() to it.getValue().map { it.toString() } }
                .toMap()
    }
}