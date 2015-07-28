package com.github.jsocle.requests.session

import com.github.jsocle.JSocleConfig
import org.junit.Assert
import org.junit.Test

public class StringSessionTest {
    Test(expected = UnsupportedOperationException::class)
    fun testUnsupportedError() {
        val session = StringSession(null, JSocleConfig())
        session["name"] = "John"
    }

    Test
    fun testSecure() {
        val jScoleConfig = JSocleConfig("Secret key".toByteArray())
        val sessionForSerialize = StringSession(null, jScoleConfig)
        sessionForSerialize["name"] = "John"
        val serialized = sessionForSerialize.serialize()!!
        val (value, mac) = serialized.split('.')
        val modifiedValue = (value.decodeBase64UrlSafe + "modify".toByteArray().asList()).encodeBase64UrlSafe
        val session = StringSession("${modifiedValue}.${mac}", jScoleConfig)
        Assert.assertFalse(session.contains("name"))
    }
}