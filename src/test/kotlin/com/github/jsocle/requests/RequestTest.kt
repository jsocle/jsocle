package com.github.jsocle.requests

import com.github.jsocle.Blueprint
import com.github.jsocle.JSocle
import com.github.jsocle.request
import org.junit.Assert
import org.junit.Test

public class RequestTest {
    private object childApp : Blueprint() {
        val index = route("/") { -> }
    }

    private object app : JSocle() {
        val index = route("/") { -> request.url }

        init {
            register(childApp, urlPrefix = "/child")
        }
    }

    @Test
    fun testUrl() {
        Assert.assertEquals("/", app.client.get("/").data)
    }

    @Test
    fun testParameters() {
        app.client.get("/") {
            Assert.assertEquals(mapOf<String, List<String>>(), request.parameters)
        }

        app.client.get("/?name=john&job=cook") {
            Assert.assertEquals(mapOf("name" to listOf("john"), "job" to listOf("cook")), request.parameters)
        }
    }

    @Test
    fun testParameter() {
        app.client.get("/") {
            Assert.assertEquals(null, request.parameter("name"))
        }

        app.client.get("/?name=john&job=cook") {
            Assert.assertEquals("john", request.parameter("name"))
            Assert.assertEquals("cook", request.parameter("job"))
        }
    }

    @Test
    fun testHandler() {
        app.client.get(app.index.url()) {
            Assert.assertSame(app.index, request.handler)
            Assert.assertArrayEquals(arrayOf(app), request.handlerCallStack)
        }

        app.client.get(childApp.index.url()) {
            Assert.assertEquals(childApp.index, request.handler)
            Assert.assertArrayEquals(arrayOf(app, childApp), request.handlerCallStack)
        }
    }
}