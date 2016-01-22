package com.github.jsocle.requests

import com.github.jsocle.Blueprint
import com.github.jsocle.JSocleApp
import java.net.URLEncoder

abstract public class RequestHandler<R>(public val app: JSocleApp, rule: String) {
    public val rule: RouteRule = RouteRule(rule)
    abstract fun handle(request: RequestImpl): R
    protected val absoluteRule: String by lazy(LazyThreadSafetyMode.NONE) {
        val app = this.app
        if (app is Blueprint) {
            app.urlPrefixes + this.rule.rule
        } else {
            this.rule.rule;
        }
    }

    private val absolutePathVariables by lazy(LazyThreadSafetyMode.NONE) {
        RouteRule(absoluteRule).variableNames
    }

    public fun url(vararg pairs: Pair<String, Any>): String {
        val map = hashMapOf<String, MutableList<Any>>()
        pairs.forEach {
            map.getOrPut(it.first) { arrayListOf() }.add(it.second)
        }
        return url(map)
    }

    public fun url(params: Map<String, List<Any>>): String {
        val url = absoluteRule.replace("<([^>:]+)(:[^>]*)?>".toRegex()) {
            val name = it.groups[1]!!.value
            if (name !in params) {
                throw NotEnoughVariables("Missing variable <$name> for $absoluteRule")
            }
            params[name]!!.first().toString()
        }
        return url + buildQueryString(params)
    }

    private fun buildQueryString(params: Map<String, List<Any>>): String {
        val queryParams = params.filter { it.key !in absolutePathVariables }
        if (queryParams.size == 0) {
            return ""
        }
        val queryString = queryParams.flatMap {
            it.value.map { v ->
                URLEncoder.encode(it.key, "UTF-8") + "=" + URLEncoder.encode(v.toString(), "UTF-8")
            }
        }.joinToString("&")
        return "?" + queryString
    }

    public class NotEnoughVariables(message: String) : RuntimeException(message)
}
