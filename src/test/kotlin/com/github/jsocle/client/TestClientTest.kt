package com.github.jsocle.client

import com.github.jsocle.Blueprint
import com.github.jsocle.JSocle
import com.github.jsocle.request
import org.junit.Assert
import org.junit.Test

public class TestClientTest {
    object sessionApp : Blueprint() {
        val get = route("/get") { -> request.session["store"] as String }
        val set = route("/set") { -> request.session["store"] = request.parameter("store")!! }
    }

    object app : JSocle() {
        init {
            config.secretKey = "secret".toByteArray()
            route("/") { ->
                return@route "index"
            }
            register(sessionApp, "/session")
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
}