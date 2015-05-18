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

    Test
    fun testParameters() {
        app.client.get("/") {
            Assert.assertEquals(mapOf<String, List<String>>(), request.parameters)
        }

        app.client.get("/?name=john&job=cook") {
            Assert.assertEquals(mapOf("name" to listOf("john"), "job" to listOf("cook")), request.parameters)
        }
    }

    Test
    fun testParameter() {
        app.client.get("/") {
            Assert.assertEquals(null, request.parameter("name"))
        }

        app.client.get("/?name=john&job=cook") {
            Assert.assertEquals("john", request.parameter("name"))
            Assert.assertEquals("cook", request.parameter("job"))
        }
    }
}