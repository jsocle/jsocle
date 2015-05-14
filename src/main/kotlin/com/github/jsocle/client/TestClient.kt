package com.github.jsocle.client

import com.github.jsocle.Klask
import com.github.jsocle.response.Response

class TestClient(private val app: Klask) : Client() {
    override fun get(url: String): Response {
        val httpServletResponse = TestHttpServletResponse()
        app.processRequest(TestHttpServletRequest(url), httpServletResponse)
        return httpServletResponse.response
    }
}
