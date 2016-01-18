package com.github.jsocle.client

import com.github.jsocle.JSocle
import com.github.jsocle.requests.Request
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

class TestClient(private val app: JSocle) : Client() {
    var cookies: List<Cookie>? = null

    override fun get(url: String, method: Request.Method): ClientResponse {
        val httpServletResponse = TestHttpServletResponse()
        app.processRequest(TestHttpServletRequest(url, cookies), httpServletResponse, method)
        cookies = httpServletResponse.cookies
        val clientResponse = httpServletResponse.buildResponse(url)
        if (clientResponse.statusCode == HttpServletResponse.SC_FOUND) {
            return get(clientResponse.headers["Location"]!![0])
        }
        return clientResponse
    }

    fun get(url: String, method: Request.Method = Request.Method.GET, intent: () -> Unit) {
        app.requestContext(TestHttpServletRequest(url, cookies), method) { request, match, hookResponse ->
            intent()
        }
    }
}

