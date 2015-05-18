package com.github.jsocle.requests

public trait Request {
    public val pathVariables: Map<String, Any>
    public val url: String
    public val parameters: Map<String, List<String>>;
    public fun parameter(name: String): String?
}
