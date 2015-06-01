package com.github.jsocle.requests

import javax.servlet.http.HttpServletRequest
import kotlin.properties.Delegates

public class RequestImpl(public override val url: String,
                         public override val pathVariables: Map<String, Any>,
                         private val httpServletRequest: HttpServletRequest,
                         public override val method: Request.Method) : Request {
    override fun parameter(name: String): String? {
        return parameters[name]?.firstOrNull()
    }

    override val parameters: Map<String, List<String>> by Delegates.lazy {
        (httpServletRequest.getParameterMap() ?: mapOf<String, Array<String>>())
                .map { it.getKey() to it.getValue().toList() }
                .toMap()
    }
}