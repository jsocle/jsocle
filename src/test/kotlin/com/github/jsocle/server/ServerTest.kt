package com.github.jsocle.server

import com.github.jsocle.JSocle
import com.github.jsocle.html.elements.H1
import com.github.jsocle.staticPath
import org.junit.Assert
import org.junit.Test

class ServerTest {
    object app : JSocle(staticPath = staticPath)

    @Test
    fun testStatic() {
        app.run {
            Assert.assertEquals("Hello, world!", app.server.client.get("/static/").data)
        }
    }
}

fun main(args: Array<String>) {
    val app = object : JSocle(staticPath = staticPath) {
        init {
            route("/") { -> "Hello, world!" }
            route("/users/<name>") { name: String -> "Hello, $name" }
            route("/korean") { -> "안녕하세요." }
            route("/korean.html") { -> H1(text_ = "안녕하세요.") }
        }
    }
    app.run()
}
