package com.github.jsocle.requests

import org.junit.Assert
import org.junit.Test

public class PrefixRuleTest {
    @Test
    fun testPrefix() {
        val prefixRule = PrefixRule("/user")
        Assert.assertEquals(null, prefixRule.match("/"))
        Assert.assertEquals(PrefixRule.MatchResult(linkedMapOf(), ""), prefixRule.match("/user"))
        Assert.assertEquals(PrefixRule.MatchResult(linkedMapOf(), "/10/show"), prefixRule.match("/user/10/show"))
    }

    @Test
    fun testWithVariables() {
        val rule = PrefixRule("/user/<name>")
        Assert.assertEquals(
                PrefixRule.MatchResult(linkedMapOf("name" to "Steve Jobs"), "/show"),
                rule.match("/user/Steve Jobs/show")
        )
    }
}