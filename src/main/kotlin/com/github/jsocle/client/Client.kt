package com.github.jsocle.client

import com.github.jsocle.requests.Request

abstract public class Client {
    operator abstract fun get(url: String, method: Request.Method = Request.Method.GET): ClientResponse
}