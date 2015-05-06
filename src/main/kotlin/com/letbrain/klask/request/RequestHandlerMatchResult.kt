package com.letbrain.klask.request

public data class RequestHandlerMatchResult(public val handler: RequestHandler<*>, public val pathVariables: Map<String, Any>)