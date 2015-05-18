package com.github.jsocle

import kotlin.platform.platformStatic

public class request {
    companion object request : com.github.jsocle.requests.Request {
        private val local = ThreadLocal<com.github.jsocle.requests.Request>()

        private val r: com.github.jsocle.requests.Request
            get() {
                val request = local.get()
                if (request == null) {
                    throw UnsupportedOperationException("Not in request context.")
                }
                return request
            }

        fun push(request: com.github.jsocle.requests.Request) {
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

        platformStatic
        override val pathVariables: Map<String, Any> get() = r.pathVariables
        platformStatic
        override val url: String get() = r.url
        platformStatic
        override val parameters: Map<String, List<String>> get() = r.parameters
        platformStatic
        override fun parameter(name: String): String? = r.parameter(name)
    }
}