package com.github.jsocle

import com.github.jsocle.form.Form
import com.github.jsocle.form.StringField
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

        val edit = route("/<id:Int>/edit") { id: Int ->
            val form = object : Form() {
                val title by StringField()
            }
            form.title.render { maxlength = "100" }
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

    object sessionApp : Blueprint() {
        val index = route("/") { ->
            val counter = if (request.parameter("counter") != null) {
                request.parameter("counter")!!
            } else if ("counter" in request.session) {
                request.session["counter"] as String
            } else "0"
            request.session["counter"] = (counter.toInt() + 1).toString()
            return@route request.session["counter"]
        }
    }

    object app : JSocle() {
        val method = route("/method") { ->
            return@route request.method.toString()
        }

        init {
            config.secretKey = "secret_key".toByteArray()
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
            register(sessionApp, urlPrefix = "/session")
        }
    }

    Test
    fun testIntegrate() {
        app.run {
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
        }
    }

    Test
    fun testSessionIntegration() {
        app.run {
            val client = app.server.client
            Assert.assertEquals("1", client.get(sessionApp.index.url()).data)
            Assert.assertEquals("2", client.get(sessionApp.index.url()).data)

            Assert.assertEquals("100", client.get(sessionApp.index.url("counter" to 99)).data)
        }
    }

    Test
    fun testForm() {
        app.run {
            val client = app.server.client
            Assert.assertEquals(
                    """<input maxlength="100" name="title" type="text">""", client.get(postApp.edit.url(1)).data
            )
        }
    }
}
