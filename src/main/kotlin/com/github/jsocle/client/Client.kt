package com.github.jsocle.client

import com.github.jsocle.response.Response

abstract public class Client {
    abstract fun get(url: String): Response
}