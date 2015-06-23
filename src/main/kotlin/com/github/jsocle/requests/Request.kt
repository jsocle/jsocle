package com.github.jsocle.requests

import com.github.jsocle.requests.session.Session

public interface Request {
    public val pathVariables: Map<String, Any>
    public val url: String
    public val parameters: Map<String, List<String>>
    public val method: Method
    public val session: Session
    public fun parameter(name: String): String?

    public enum class Method {
        GET, POST
    }
}
