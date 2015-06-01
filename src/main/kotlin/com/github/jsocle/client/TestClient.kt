package com.github.jsocle.client

import com.github.jsocle.JSocle
import com.github.jsocle.requests.Request
import com.github.jsocle.response.Response

class TestClient(private val app: JSocle) : Client() {
    override fun get(url: String, method: Request.Method): Response {
        val httpServletResponse = TestHttpServletResponse()
        app.processRequest(TestHttpServletRequest(url), httpServletResponse, method)
        return httpServletResponse.response
    }

    fun get(url: String, method: Request.Method = Request.Method.GET, intent: () -> Unit) {
        app.requestContext(TestHttpServletRequest(url), method) { request, match ->
            intent()
        }
    }
}
