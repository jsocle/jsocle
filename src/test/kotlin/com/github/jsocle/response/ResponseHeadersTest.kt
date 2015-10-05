package com.github.jsocle.response

import com.github.jsocle.JSocle
import org.junit.Assert
import org.junit.Test

class ResponseHeadersTest {
    val app = object : JSocle() {
        init {
            route("/") { ->
                makeResponse("index").apply {
                    headers["X-Test"] = arrayListOf("test-response-header")
                }
            }
        }
    }

    @Test
    fun testResponseHeaders() {
        Assert.assertEquals(arrayListOf("test-response-header"), app.client.get("/").headers["X-Test"])
        app.run {
            Assert.assertEquals(arrayListOf("test-response-header"), server.client.get("/").headers["X-Test"])
        }
    }
}