package com.github.jsocle.client

import com.github.jsocle.JSocle
import com.github.jsocle.requests.Request
import com.github.jsocle.response.Response
import javax.servlet.http.Cookie

class TestClient(private val app: JSocle) : Client() {
    var cookies: List<Cookie>? = null

    override fun get(url: String, method: Request.Method): Response {
        val httpServletResponse = TestHttpServletResponse()
        app.processRequest(TestHttpServletRequest(url, cookies), httpServletResponse, method)
        cookies = httpServletResponse.cookies
        return httpServletResponse.response
    }

    fun get(url: String, method: Request.Method = Request.Method.GET, intent: () -> Unit) {
        app.requestContext(TestHttpServletRequest(url, cookies), method) { request, match ->
            intent()
        }
    }
}
