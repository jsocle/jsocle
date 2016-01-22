package com.github.jsocle

import org.junit.Assert
import org.junit.Test
import kotlin.collections.arrayListOf
import kotlin.collections.last
import kotlin.collections.listOf

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

    @Test
    fun testOnBeforeRequest() {
        var onBeforeCallStack: MutableList<JSocleApp> = arrayListOf()

        val childApp = object : Blueprint() {
            val index = route("/") { -> }
            val forbidden = route("/forbidden") { -> }

            override fun onBeforeRequest(): Any? {
                onBeforeCallStack.add(this)
                return super.onBeforeRequest()
            }
        }

        val app = object : JSocle() {
            val index = route("/") { -> }

            init {
                register(childApp, urlPrefix = "/childApp")
            }

            override fun onBeforeRequest(): Any? {
                onBeforeCallStack.add(this)
                if (request.handlerCallStack.last() == childApp) {
                    if (request.handler == childApp.forbidden) {
                        return "forbidden"
                    }
                }
                return null
            }
        }

        onBeforeCallStack = arrayListOf()
        app.client.get(app.index.url())
        Assert.assertEquals(listOf(app), onBeforeCallStack)

        onBeforeCallStack = arrayListOf()
        app.client.get(childApp.index.url())
        Assert.assertEquals(listOf(app, childApp), onBeforeCallStack)

        onBeforeCallStack = arrayListOf()
        app.client.get(childApp.forbidden.url())
        Assert.assertEquals(listOf(app), onBeforeCallStack)
    }

    @Test
    fun testOnBeforeRequestEarlyReturn() {
        val app = object : JSocle() {
            val index = route("/") { -> "index" }

            override fun onBeforeRequest(): Any? {
                return "onBeforeRequest"
            }
        }

        Assert.assertEquals("onBeforeRequest", app.client.get(app.index.url()).data)
    }
}