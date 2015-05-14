package com.github.jsocle.client

import com.github.jsocle.response.Response
import com.github.jsocle.response.StaticResponse
import java.net.URL

public class HttpClient(private val port: Int) : Client() {
    override fun get(url: String): Response {
        URL("http://localhost:${port}${url}").openConnection().getInputStream().use {
            return StaticResponse(it.reader().readText())
        }
    }
}