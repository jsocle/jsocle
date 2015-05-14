package com.github.jsocle

import com.github.jsocle.requests.Request

public object request : Request {
    private val local = ThreadLocal<Request>()

    private val r: Request
        get() {
            val request = local.get()
            if (request == null) {
                throw UnsupportedOperationException("Not in request context.")
            }
            return request
        }

    fun push(request: Request) {
        if (local.get() != null) {
            throw UnsupportedOperationException("Request context was already settled.")
        }
        local.set(request)
    }

    fun pop() {
        if (local.get() == null) {
            throw UnsupportedOperationException("Request context was not settled.")
        }
        local.remove()
    }

    override val pathVariables: Map<String, Any> get() = r.pathVariables
}