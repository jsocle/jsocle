package com.letbrain.klask.request

abstract public class RequestHandler<R>(public val rule: String) {
    abstract public fun handle(): R
}