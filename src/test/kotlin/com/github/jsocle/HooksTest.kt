package com.github.jsocle

import org.junit.Assert
import org.junit.Test

class HooksTest {
    @Test
    fun testOnBeforeFirstRequest() {
        val app = object : JSocle() {
            init {
                route("/") { -> }
            }
       }

        var onBeforeFirstRequestCalled = false
        app.addOnBeforeFirstRequest {
            onBeforeFirstRequestCalled = true
        }

        app.client.get("/")
        Assert.assertTrue(onBeforeFirstRequestCalled)
    }

    @Test
    fun testTeardownRequest() {
        val app = object : JSocle() {
            init {
                route("/") { -> throw RuntimeException() }
            }
        }

        var teardownRequestCalled = false
        app.addOnTeardownRequest { teardownRequestCalled = true }

        var runtimeExceptionCatched = false
        try {
            app.client.get("/")
        } catch (e: RuntimeException) {
            runtimeExceptionCatched = true
        }
        Assert.assertTrue(runtimeExceptionCatched)
        Assert.assertTrue(teardownRequestCalled)
    }
}