package com.github.jsocle.requests

import com.github.jsocle.JSocleApp

data class RequestHandlerMatchResult(val handler: RequestHandler<*>, val pathVariables: Map<String, Any>,
                                     val handlerStack: Array<JSocleApp>)