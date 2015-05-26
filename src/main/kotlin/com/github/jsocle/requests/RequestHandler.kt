package com.github.jsocle.requests

import com.github.jsocle.JSocleApp

abstract public class RequestHandler<R>(public val app: JSocleApp, rule: String) {
    public val rule: RouteRule = RouteRule(rule)
    abstract fun handle(request: RequestImpl): R
}