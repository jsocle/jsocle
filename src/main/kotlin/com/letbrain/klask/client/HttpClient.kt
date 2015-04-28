package com.letbrain.klask.client

import com.letbrain.klask.Response
import com.letbrain.klask.StaticResponse
import java.net.URL

public class HttpClient(private val port: Int) : Client() {
    override fun get(url: String): Response {
        URL("http://localhost:${port}${url}").openConnection().getInputStream().use {
            return StaticResponse(it.reader().readText())
        }
    }
}