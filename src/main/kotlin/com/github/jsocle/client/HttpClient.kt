package com.github.jsocle.client

import com.github.jsocle.requests.Request
import com.github.jsocle.response.Response
import com.github.jsocle.response.StaticResponse
import java.net.HttpURLConnection
import java.net.URL

public class HttpClient(private val port: Int) : Client() {
    override fun get(url: String, method: Request.Method): Response {
        val connection = URL("http://localhost:${port}${url}").openConnection() as HttpURLConnection
        connection.setRequestMethod(method.toString())
        connection.getInputStream().use {
            return StaticResponse(it.reader().readText())
        }
    }
}