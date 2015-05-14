package com.github.jsocle.requests

public data class RequestHandlerMatchResult(public val handler: RequestHandler<*>, public val pathVariables: Map<String, Any>)