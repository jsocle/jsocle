package com.letbrain.klask.request

import org.junit.Assert
import org.junit.Test

public class RuleTest {
    Test
    fun testUriMatch() {
        val rule = Rule("/")
        Assert.assertEquals(linkedMapOf<String, Any>(), rule.match("/"))
        Assert.assertNull(rule.match("/page"))
    }

    Test
    fun testPathVariables() {
        Assert.assertEquals(null, Rule("/<name>").match("/"))
        Assert.assertEquals(linkedMapOf("name" to "steve jobs"), Rule("/<name>").match("/steve jobs"))
        Assert.assertEquals(
                linkedMapOf("author" to "steve jobs", "article" to "Stay foolish, Stay hungry"),
                Rule("/<author>/<article>").match("/steve jobs/Stay foolish, Stay hungry")
        )
    }

    Test
    fun testVariableNames() {
        Assert.assertEquals(linkedSetOf<String>(), Rule("/").variables);
        Assert.assertEquals(linkedSetOf("name"), Rule("/<name>").variables);
    }
}