package com.github.jsocle.requests

import com.github.jsocle.JSocle
import com.github.jsocle.request
import org.junit.Assert
import org.junit.Test

public class RequestContextTest {
    private object app : JSocle() {
        init {
            route("/") { -> }
        }
    }

    @Test
    fun testContext() {
        app.client.get("/") {
            Assert.assertEquals("/", request.url)
        }
    }
}