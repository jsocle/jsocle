package com.letbrain.klask

import org.junit.Assert
import org.junit.Test

public class KlaskTest {
    Test
    fun testInit() {
        val app = Klask()
        app.init()
        Assert.assertTrue(app.initialized)
    }
}