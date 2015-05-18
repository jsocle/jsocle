package com.github.jsocle.requests

import com.github.jsocle.JSocle
import com.github.jsocle.request
import org.junit.Assert
import org.junit.Test

public class RequestTest {
    private object app : JSocle() {
        init {
            route("/") { ->
                return@route request.url
            }
        }
    }

    Test
    fun testUrl() {
        Assert.assertEquals("/", app.client.get("/").data)
    }
}