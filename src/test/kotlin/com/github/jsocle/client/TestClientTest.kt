package com.github.jsocle.client

import com.github.jsocle.Blueprint
import com.github.jsocle.JSocle
import com.github.jsocle.request
import org.junit.Assert
import org.junit.Test
import kotlin.text.toByteArray

public class TestClientTest {
    object sessionApp : Blueprint() {
        val get = route("/get") { -> request.session["store"] as String }
        val set = route("/set") { -> request.session["store"] = request.parameter("store")!! }
    }

    object redirectApp : Blueprint() {
        val from = route("/from") { -> redirect(to.url()) }
        val to = route("/to") { -> "redirected" }
    }

    object app : JSocle() {
        init {
            config.secretKey = "secret".toByteArray()
            route("/") { -> "index" }
            register(sessionApp, "/session")
            register(redirectApp, "/redirect")
        }
    }

    @Test
    fun testGet() {
        Assert.assertEquals("index", app.client.get("/").data)
    }

    @Test
    fun testStringSession() {
        val client = app.client
        client.get(sessionApp.set.url("store" to "a value"))
        Assert.assertEquals("a value", client.get(sessionApp.get.url()).data)
    }

    @Test
    fun testUrl() {
        Assert.assertEquals("/", app.client.get("/").url)
    }

    @Test
    fun testRedirect() {
        val res = app.client.get(redirectApp.from.url())
        Assert.assertEquals(redirectApp.to.url(), res.url)
        Assert.assertEquals("redirected", res.data)
    }
}