package com.github.jsocle.response

import com.github.jsocle.JSocle
import com.github.jsocle.html.elements.H1
import org.junit.Assert
import org.junit.Test

class ResponseContentTypeTest {
    @Test
    fun testText() {
        val app = object : JSocle() {
            init {
                route("/") { -> "Hello, world!" }
            }
        }
        Assert.assertEquals(arrayListOf("text/html; charset=utf-8"), app.client.get("/").headers["Content-Type"])
    }

    @Test
    fun testNode() {
        val app = object : JSocle() {
            init {
                route("/") { -> H1("Hello, world!") }
            }
        }
        Assert.assertEquals(arrayListOf("text/html; charset=utf-8"), app.client.get("/").headers["Content-Type"])
    }

}