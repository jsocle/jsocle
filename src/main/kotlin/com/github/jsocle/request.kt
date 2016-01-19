package com.github.jsocle

import com.github.jsocle.requests.Request
import com.github.jsocle.requests.RequestHandler
import com.github.jsocle.requests.session.Session
import javax.servlet.http.HttpServletRequest
import kotlin.collections.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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
        override val handler: RequestHandler<*> get() = r.handler
        @JvmStatic
        override val handlerCallStack: Array<JSocleApp> get() = r.handlerCallStack
        @JvmStatic
        override val g: Request.RequestGlobal get() = r.g

        fun <T> g(): ReadWriteProperty<Any, T> {
            return object : ReadWriteProperty<Any, T> {
                override fun getValue(thisRef: Any, property: KProperty<*>): T {
                    @Suppress("UNCHECKED_CAST")
                    return g[property.name] as T
                }

                override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                    g[property.name] = value
                }
            }
        }

        fun <T> g(default: () -> T): ReadWriteProperty<Any, T> {
            return object : ReadWriteProperty<Any, T> {
                override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                    g[property.name] = value
                }

                @Suppress("UNCHECKED_CAST")
                override fun getValue(thisRef: Any, property: KProperty<*>): T {
                    return g.getOrSet(property.name, default) as T
                }
            }
        }

        @JvmStatic
        override fun parameter(name: String): String? = r.parameter(name)
    }
}