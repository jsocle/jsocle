package com.letbrain.klask.client

import com.letbrain.klask.Klask
import com.letbrain.klask.response.Response

class TestClient(private val app: Klask) : Client() {
    override fun get(url: String): Response {
        val httpServletResponse = TestHttpServletResponse()
        app.processRequest(TestHttpServletRequest(url), httpServletResponse)
        return httpServletResponse.response
    }
}
