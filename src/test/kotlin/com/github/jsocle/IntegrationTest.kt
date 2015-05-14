package com.github.jsocle

import com.github.jsocle.html.elements.Ul
import org.junit.Assert
import org.junit.Test

public class IntegrationTest {
    class FaqApp : Blueprint() {
        class License : Blueprint() {
            init {
                route("/license") { ->
                    return@route "licenseIndex"
                }
            }

        }

        init {
            route("/") { ->
                return@route "faqIndex"
            }

            register(License())
        }
    }

    class ProfileApp : Blueprint() {
        init {
            route("/") { ->
                return@route "profile@"
            }
        }
    }

    object app : JSocle() {
        init {
            route("/") { ->
                return@route "Hello, World!"
            }

            route("/hello/<name>") { name: String ->
                return@route "Hello, ${name}!"
            }

            route("/hello/<name>/<many:Int>") { name: String, many: Int ->
                return@route Ul {
                    many.times { li("Hello, ${name}!") }
                }
            }

            register(FaqApp(), urlPrefix = "/faq")
            register(ProfileApp(), urlPrefix = "/profile/<userId:Int>")
        }
    }

    Test
    fun testIntegrate() {
        app.run(onBackground = true)

        Assert.assertEquals("Hello, World!", app.server.client.get("/").data)
        Assert.assertEquals("Hello, Steve Jobs!", app.server.client.get("/hello/Steve%20Jobs").data)
        Assert.assertEquals(
                "<ul><li>Hello, Steve Jobs!</li><li>Hello, Steve Jobs!</li><li>Hello, Steve Jobs!</li></ul>",
                app.server.client.get("/hello/Steve%20Jobs/3").data
        )

        Assert.assertEquals("faqIndex", app.server.client.get("/faq/").data)
        Assert.assertEquals("licenseIndex", app.server.client.get("/faq/license").data)

        app.stop()
    }
}
