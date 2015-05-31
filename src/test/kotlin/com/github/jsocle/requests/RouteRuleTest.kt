package com.github.jsocle.requests

import org.junit.Assert
import org.junit.Test

public class RouteRuleTest {
    Test
    fun testUriMatch() {
        val rule = RouteRule("/")
        Assert.assertEquals(linkedMapOf<String, Any>(), rule.match("/"))
        Assert.assertNull(rule.match("/page"))
    }

    Test
    fun testPathVariables() {
        Assert.assertEquals(null, RouteRule("/<name>").match("/"))
        Assert.assertEquals(linkedMapOf("name" to "steve jobs"), RouteRule("/<name>").match("/steve jobs"))
        Assert.assertEquals(
                linkedMapOf("author" to "steve jobs", "article" to "Stay foolish, Stay hungry"),
                RouteRule("/<author>/<article>").match("/steve jobs/Stay foolish, Stay hungry")
        )
    }

    Test
    fun testVariableNames() {
        Assert.assertEquals(linkedSetOf<String>(), RouteRule("/").variableNames);
        Assert.assertEquals(linkedSetOf("name", "id"), RouteRule("/<name>/<id:Int>").variableNames);

        Assert.assertEquals(listOf<String>(), RouteRule("/").variableNameList);
        Assert.assertEquals(listOf("name", "id"), RouteRule("/<name>/<id:Int>").variableNameList);
    }

    Test
    fun testInt() {
        Assert.assertEquals(null, RouteRule("/<id:Int>").match("/name"))
        Assert.assertEquals(linkedMapOf("id" to 1), RouteRule("/<id:Int>").match("/1"))
    }
}