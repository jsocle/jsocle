package com.github.jsocle

import com.github.jsocle.html.elements.Ul
import com.github.jsocle.requests.Request
import org.junit.Assert
import org.junit.Test

public class IntegrationTest {
    object faqApp : Blueprint() {
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

    object profileApp : Blueprint() {
        init {
            route("/") { ->
                return@route "profile@"
            }
        }
    }

    object postApp : Blueprint() {
        class Post(val id: Int, val title: String)

        val posts = listOf("tutorial").mapIndexed { i, title -> Post(i + 1, title) }

        val show = route("/<id:Int>") { id: Int ->
            return@route posts.first { it.id == id }.title
        }

        init {
            route("/") { ->
                return@route Ul {
                    posts.forEach {
                        li {
                            a(text_ = it.title, href = show.url(it.id))
                        }
                    }
                }
            }
        }
    }

    object app : JSocle() {
        val method = route("/method") { ->
            return@route request.method.toString()
        }

        init {
            route("/") { ->
                return@route "Hello, World!"
            }

            route("/hello/<name>") { name: String ->
                return@route "Hello, ${name}!"
            }

            route("/hello/<name>/<many:Int>") { name: String, many: Int ->
                return@route Ul {
                    repeat(many) { li("Hello, ${name}!") }
                }
            }

            register(faqApp, urlPrefix = "/faq")
            register(profileApp, urlPrefix = "/profile/<userId:Int>")
            register(postApp, urlPrefix = "/post")
        }
    }

    Test
    fun testIntegrate() {
        app.run(onBackground = true)

        try {
            Assert.assertEquals("Hello, World!", app.server.client.get("/").data)
            Assert.assertEquals("Hello, Steve Jobs!", app.server.client.get("/hello/Steve%20Jobs").data)
            Assert.assertEquals(
                    "<ul><li>Hello, Steve Jobs!</li><li>Hello, Steve Jobs!</li><li>Hello, Steve Jobs!</li></ul>",
                    app.server.client.get("/hello/Steve%20Jobs/3").data
            )

            Assert.assertEquals("faqIndex", app.server.client.get("/faq/").data)
            Assert.assertEquals("licenseIndex", app.server.client.get("/faq/license").data)

            Assert.assertEquals("<ul><li><a href=\"/post/1\">tutorial</a></li></ul>", app.server.client.get("/post/").data)

            Assert.assertEquals("GET", app.server.client.get(app.method.url()).data)
            Assert.assertEquals("POST", app.server.client.get(app.method.url(), Request.Method.POST).data)
        } finally {
            app.stop()
        }
    }
}
