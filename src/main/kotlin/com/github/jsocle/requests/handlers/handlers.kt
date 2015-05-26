package com.github.jsocle.requests.handlers

import com.github.jsocle.Blueprint
import com.github.jsocle.JSocleApp
import com.github.jsocle.requests.RequestHandler
import com.github.jsocle.requests.RequestImpl

public class RequestHandler0<R>(app: JSocleApp, rule: String, private val handler: () -> R) : RequestHandler<R>(app, rule) {
    override fun handle(request: RequestImpl): R {
        return handler()
    }

    fun url(): String {
        if (app is Blueprint) {
            return app.urlPrefixes + rule.rule
        }
        return rule.rule;
    }
}

public class RequestHandler1<R, P1>(app: JSocleApp, rule: String, private val handler: (p1: P1) -> R) : RequestHandler<R>(app, rule) {
    suppress("UNCHECKED_CAST")
    override fun handle(request: RequestImpl): R {
        val p1 = request.pathVariables[this.rule.variableNames.first()] as P1
        return handler(p1)
    }

    fun urlFor(p1: P1): String {
        return ""
    }
}
