package com.github.jsocle

import com.github.jsocle.requests.Request
import com.github.jsocle.requests.session.Session
import javax.servlet.http.HttpServletRequest

public class request {
    companion object request : com.github.jsocle.requests.Request {
        private val local = ThreadLocal<com.github.jsocle.requests.Request>()

        private val r: com.github.jsocle.requests.Request
            get() = local.get() ?: throw UnsupportedOperationException("Not in request context.")


        private fun push(request: com.github.jsocle.requests.Request) {
            if (local.get() != null) {
                throw UnsupportedOperationException("Request context was already settled.")
            }
            local.set(request)
        }

        private fun pop() {
            if (local.get() == null) {
                throw UnsupportedOperationException("Request context was not settled.")
            }
            local.remove()
        }

        operator fun invoke(request: com.github.jsocle.requests.Request, intent: () -> Unit) {
            push(request)
            try {
                intent()
            } finally {
                pop()
            }
        }

        @JvmStatic
        override val pathVariables: Map<String, Any> get() = r.pathVariables
        @JvmStatic
        override val url: String get() = r.url
        @JvmStatic
        override val parameters: Map<String, List<String>> get() = r.parameters
        @JvmStatic
        override val method: Request.Method get() = r.method
        @JvmStatic
        public override val session: Session get() = r.session
        @JvmStatic
        override val servlet: HttpServletRequest get() = r.servlet

        @JvmStatic
        override fun parameter(name: String): String? = r.parameter(name)
    }
}