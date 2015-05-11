package com.letbrain.klask

import com.khtml.elements.Ul
import org.junit.Assert
import org.junit.Test

public class IntegrationTest {
    object app : Klask() {
        init {
            route("/") { ->
                return@route "Hello, World!"
            }

            route("/<name>") { name: String ->
                return@route "Hello, ${name}!"
            }

            route("/<name>/<many:Int>") { name: String, many: Int ->
                return@route Ul {
                    many.times { li("Hello, ${name}!") }
                }
            }
        }
    }

    Test
    fun testIntegrate() {
        app.run(onBackground = true)
        Assert.assertEquals("Hello, World!", app.server.client.get("/").data)
        Assert.assertEquals("Hello, Steve Jobs!", app.server.client.get("/Steve%20Jobs").data)
        Assert.assertEquals("<ul><li>Hello, Steve Jobs!</li><li>Hello, Steve Jobs!</li><li>Hello, Steve Jobs!</li></ul>", app.server.client.get("/Steve%20Jobs/3").data)
        app.stop()
    }
}
