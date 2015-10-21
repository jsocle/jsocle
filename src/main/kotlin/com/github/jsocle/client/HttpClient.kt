package com.github.jsocle.client

import com.github.jsocle.requests.Request
import java.net.HttpURLConnection
import java.net.URL

public class HttpClient(private val port: Int) : Client() {
    var cookies: String? = null

    operator override fun get(url: String, method: Request.Method): ClientResponse {
        val connection = URL("http://localhost:$port$url").openConnection() as HttpURLConnection
        connection.requestMethod = method.toString()
        if (cookies != null) {
            connection.setRequestProperty("Cookie", cookies);
        }

        val cookieField = connection.getHeaderField("Set-Cookie")
        if (cookieField != null) {
            cookies = cookieField;
        }

        connection.inputStream.use {
            return ClientResponse(
                    it.reader().readText(), connection.headerFields, connection.url.file, connection.responseCode
            )
        }
    }
}