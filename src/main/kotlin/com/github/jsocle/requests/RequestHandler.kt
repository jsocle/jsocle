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

    public fun url(vararg pairs: Pair<String, Any>): String {
        return url(pairs.toList().toMap())
    }

    public fun url(params: Map<String, Any>): String {
        return absoluteRule.replaceAll("<([^>:]+)(:[^>]*)?>") {
            val name = it.group(1)
            if (name !in params) {
                throw NotEnoughVariables("Missing variable <$name> for $absoluteRule")
            }
            params[name].toString()
        }
    }

    public class NotEnoughVariables(message: String) : RuntimeException(message)
}
