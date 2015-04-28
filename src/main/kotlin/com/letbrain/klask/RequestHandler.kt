package com.letbrain.klask

abstract public class RequestHandler<R>(public val rule: String) {
    abstract public fun handle(): R
}