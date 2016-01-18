package com.github.jsocle.requests.session

import com.github.jsocle.JSocleConfig
import org.junit.Assert
import org.junit.Test
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.plus
import kotlin.text.split
import kotlin.text.toByteArray

public class StringSessionTest {
    @Test(expected = UnsupportedOperationException::class)
    fun testUnsupportedError() {
        val session = StringSession(null, JSocleConfig())
        session["name"] = "John"
    }

    @Test
    fun testSecure() {
        val jScoleConfig = JSocleConfig("Secret key".toByteArray())
        val sessionForSerialize = StringSession(null, jScoleConfig)
        sessionForSerialize["name"] = "John"
        val serialized = sessionForSerialize.serialize()!!
        val (value, mac) = serialized.split('.')
        val modifiedValue = (value.decodeBase64UrlSafe + "modify".toByteArray()).encodeBase64UrlSafe
        val session = StringSession("$modifiedValue.$mac", jScoleConfig)
        Assert.assertFalse(session.contains("name"))
    }

    @Test
    fun testRemove() {
        val session = StringSession(null, JSocleConfig("key".toByteArray()))
        session["key"] = "value"
        Assert.assertTrue("key" in session)
        session.remove("key")
        Assert.assertTrue("key" !in session)
    }
}