package com.github.jsocle.client

import com.github.jsocle.requests.Request
import com.github.jsocle.response.Response

abstract public class Client {
    abstract fun get(url: String, method: Request.Method = Request.Method.GET): Response
}