package com.letbrain.klask.request

abstract public class RequestHandler<R>(rule: String) {
    public val rule: Rule = Rule(rule)
    abstract public fun handle(request: RequestImpl): R
}