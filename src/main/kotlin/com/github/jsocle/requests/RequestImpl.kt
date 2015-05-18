package com.github.jsocle.requests

public class RequestImpl(public override val url: String,
                         public override val pathVariables: Map<String, Any>) : Request {
}