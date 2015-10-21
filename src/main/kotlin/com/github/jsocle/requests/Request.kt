package com.github.jsocle.requests

import com.github.jsocle.JSocleApp
import com.github.jsocle.requests.session.Session
import java.util.*
import javax.servlet.http.HttpServletRequest

interface Request {
    val pathVariables: Map<String, Any>
    val url: String
    val parameters: Map<String, List<String>>
    val method: Method
    val session: Session
    val servlet: HttpServletRequest
    val handler: RequestHandler<*>
    val handlerCallStack: Array<JSocleApp>
    val g: RequestGlobal

    fun parameter(name: String): String?

    enum class Method {
        GET, POST
    }

    class RequestGlobal : HashMap<String, Any?>() {
        fun getOrSet(key: String, default: () -> Any?): Any? {
            if (key !in this) {
                this[key] = default()
            }
            return this[key]
        }
    }
}
