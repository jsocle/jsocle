package com.github.jsocle.server

import com.github.jsocle.JSocle
import com.github.jsocle.staticPath
import org.junit.Assert
import org.junit.Test

class ServerTest {
    object app : JSocle(staticPath = staticPath)

    Test
    fun testStatic() {
        app.run {
            Assert.assertEquals("Hello, world!", app.server.client.get("/static/").data)
        }
    }
}

fun main(args: Array<String>) {
    val app = object : JSocle(staticPath = staticPath) {
        init {
            route("/") { ->
                return@route "Hello, world!"
            }

            route("/users/<name>") { name: String ->
                return@route "Hello, ${name}"
            }
        }
    }
    app.run()
}
