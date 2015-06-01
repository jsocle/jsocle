package com.github.jsocle.requests

public interface Request {
    public val pathVariables: Map<String, Any>
    public val url: String
    public val parameters: Map<String, List<String>>
    public val method: Method
    public fun parameter(name: String): String?

    public enum class Method {
        GET, POST
    }
}
