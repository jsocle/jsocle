package com.letbrain.klask.server

import com.letbrain.klask.Klask
import org.junit.Assert
import org.junit.Test
import java.nio.file.Paths

class ServerTest {
    object app : Klask(Paths.get("src/test/resources/static").toAbsolutePath())

    Test
    fun testStatic() {
        app.run(onBackground = true)
        Assert.assertEquals("Hello, world!", app.server.client.get("/static/").data)
        app.stop()
    }
}