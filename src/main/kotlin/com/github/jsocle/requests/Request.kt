package com.github.jsocle.requests

public trait Request {
    public val pathVariables: Map<String, Any>
    public val url: String
}
