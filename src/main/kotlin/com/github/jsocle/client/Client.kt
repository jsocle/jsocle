package com.letbrain.klask.client

import com.letbrain.klask.response.Response

abstract public class Client {
    abstract fun get(url: String): Response
}