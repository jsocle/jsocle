package com.github.jsocle.server

import com.github.jsocle.Klask
import com.github.jsocle.staticPath
import org.junit.Assert
import org.junit.Test

class ServerTest {
    object app : Klask(staticPath = staticPath)

    Test
    fun testStatic() {
        app.run(onBackground = true)
        Assert.assertEquals("Hello, world!", app.server.client.get("/static/").data)
        app.stop()
    }
}

fun main(args: Array<String>) {
    val app = object : Klask(staticPath = staticPath) {
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
