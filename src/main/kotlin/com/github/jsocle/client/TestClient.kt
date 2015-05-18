package com.github.jsocle.client

import com.github.jsocle.JSocle
import com.github.jsocle.requests.RequestHandlerMatchResult
import com.github.jsocle.requests.RequestImpl
import com.github.jsocle.response.Response

class TestClient(private val app: JSocle) : Client() {
    override fun get(url: String): Response {
        val httpServletResponse = TestHttpServletResponse()
        app.processRequest(TestHttpServletRequest(url), httpServletResponse)
        return httpServletResponse.response
    }

    fun get(url: String, intent: () -> Unit) {
        app.requestContext(TestHttpServletRequest(url)) { request,  match ->
            intent()
        }
    }
}
