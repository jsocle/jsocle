package com.github.jsocle.requests

import com.github.jsocle.Blueprint
import com.github.jsocle.JSocleApp
import kotlin.properties.Delegates

abstract public class RequestHandler<R>(public val app: JSocleApp, rule: String) {
    public val rule: RouteRule = RouteRule(rule)
    abstract fun handle(request: RequestImpl): R
    protected val absoluteRule: String by Delegates.lazy {
        val app = this.app
        if (app is Blueprint) {
            app.urlPrefixes + this.rule.rule
        } else {
            this.rule.rule;
        }
    }

    public fun url(params: Map<String, Any>): String {
        return absoluteRule.replaceAll("<([^>:]+)(:[^>]*)?>") { params[it.group(1)].toString() }
    }
}