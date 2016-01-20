package com.github.jsocle.response

import com.github.jsocle.JSocle
import com.github.jsocle.request
import com.github.jsocle.requests.get
import org.junit.Assert
import org.junit.Test
import javax.servlet.http.Cookie

class ResponseTest {
    @Test
    fun testCookie() {
        val app = object : JSocle() {
            var index = route("/") { ->
                request.cookies["test"]?.value
            }
            val burn = route("/burn/") { ->
                makeResponse(null).apply {
                    cookies.add(Cookie("test", "value"))
                }
            }
            var burnCheck = route("/burn/check") { -> request.cookies["test"]?.value }
        }

        app.run {
            val client = app.server.client
            Assert.assertEquals("", client.get(app.index.url()).data)
            Assert.assertEquals("", client.get(app.burnCheck.url()).data)

            client.get(app.burn.url())
            Assert.assertEquals("value", client.get(app.burnCheck.url()).data)
            Assert.assertEquals("", client.get(app.index.url()).data)
        }

        val client = app.client
        Assert.assertEquals("", client.get(app.index.url()).data)
        Assert.assertEquals("", client.get(app.burnCheck.url()).data)

        client.get(app.burn.url())
        Assert.assertEquals("value", client.get(app.burnCheck.url()).data)
        Assert.assertEquals("", client.get(app.index.url()).data)
    }
}