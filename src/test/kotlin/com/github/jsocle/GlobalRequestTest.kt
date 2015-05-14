package com.github.jsocle

import org.junit.Assert
import org.junit.Test

public class GlobalRequestTest {
    Test
    fun testRequestGlobal() {
        val app = object : JSocle() {
            init {
                route("/<name>") { name: String ->
                    return@route "${request.pathVariables["name"]}"
                }
            }
        }

        Assert.assertEquals("john", app.client.get("/john").data)
    }
}