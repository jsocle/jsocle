package com.github.jsocle

import com.github.jsocle.requests.Request
import com.github.jsocle.requests.session.Session
import kotlin.platform.platformStatic

public class request {
    companion object request : com.github.jsocle.requests.Request {
        private val local = ThreadLocal<com.github.jsocle.requests.Request>()

        private val r: com.github.jsocle.requests.Request
            get() = local.get() ?: throw UnsupportedOperationException("Not in request context.")

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
        override val method: Request.Method get() = r.method
        platformStatic
        public override val session: Session
            get() = r.session
        platformStatic
        override fun parameter(name: String): String? = r.parameter(name)
    }
}