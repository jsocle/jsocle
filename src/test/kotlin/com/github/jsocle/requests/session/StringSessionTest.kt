package com.github.jsocle.requests.session

import com.github.jsocle.JScoleConfig
import org.junit.Test

public class StringSessionTest {
    Test(expected = UnsupportedOperationException::class)
    fun testUnsupportedError() {
        val session = StringSession(null, JScoleConfig())
        session["name"] = "John"
    }
}