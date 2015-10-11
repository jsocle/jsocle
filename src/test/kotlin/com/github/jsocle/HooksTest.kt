package com.github.jsocle

import org.junit.Assert
import org.junit.Test

class HooksTest {
    @Test
    fun testOnBeforeFirstRequest() {
        val app = object : JSocle() {
            var onBeforeFirstRequestCalled = false

            init {
                route("/") { -> }
            }

            override fun onBeforeFirstRequest() {
                super.onBeforeFirstRequest()
                onBeforeFirstRequestCalled = true
            }
        }

        var onBeforeFirstRequestCalled = false
        app.addOnBeforeFirstRequest {
            onBeforeFirstRequestCalled = true
        }

        app.client.get("/")
        Assert.assertTrue(onBeforeFirstRequestCalled)
        Assert.assertTrue(app.onBeforeFirstRequestCalled)
    }
}