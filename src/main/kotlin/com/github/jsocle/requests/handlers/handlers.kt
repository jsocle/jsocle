package com.github.jsocle.requests.handlers

import com.github.jsocle.JSocleApp
import com.github.jsocle.requests.RequestHandler
import com.github.jsocle.requests.RequestImpl

public class RequestHandler0<R>(app: JSocleApp, rule: String, private val handler: () -> R) : RequestHandler<R>(app, rule) {
    override fun handle(request: RequestImpl): R {
        return handler()
    }

    fun url(): String {
        return url(mapOf())
    }
}

public class RequestHandler1<R, P1>(app: JSocleApp, rule: String, private val handler: (p1: P1) -> R) : RequestHandler<R>(app, rule) {
    suppress("UNCHECKED_CAST")
    override fun handle(request: RequestImpl): R {
        val p1 = request.pathVariables[this.rule.variableNameList[0]] as P1
        return handler(p1)
    }

    fun url(p1: P1): String {
        return url(mapOf(rule.variableNameList[0] to p1))
    }
}
