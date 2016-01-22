package com.github.jsocle

import com.github.jsocle.client.HttpClient
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test

class JSocleConfigTest {
    @Test
    fun testDebug() {
        val message = "Test error sign."

        try {
            val debug = object : JSocle(JSocleConfig(debug = true)) {
                init {
                    route("/") { -> throw NotImplementedError(message) }
                }
            }
            debug.run {
                println(debug.server.client.get("/"))
            }
            throw NotImplementedError("index method should throw error.")
        } catch (e: HttpClient.InternalErrorException) {
            Assert.assertThat(e.body, CoreMatchers.containsString(message))
        }

        try {
            object : JSocle() {
                init {
                    route("/") { -> throw NotImplementedError(message) }
                }
            }.apply {
                run {
                    server.client.get("/")
                }
            }
        } catch (e: HttpClient.InternalErrorException) {
            Assert.assertThat(e.body, CoreMatchers.not(CoreMatchers.containsString(message)))
        }
    }
}