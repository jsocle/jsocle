package com.letbrain.klask.request

import java.util.regex.Pattern

public class Rule(public val rule: String) {
    val variables: Set<String>
        get() {
            val matcher = Pattern.compile("<([^>]+)>").matcher(rule)
            val variables = linkedSetOf<String>()
            while (matcher.find()) {
                variables.add(matcher.group(1))
            }
            return variables
        }

    public fun match(uri: String): Map<String, Any>? {
        val s = rule.replaceAll("<[^>]+>", "([^/]+)")
        val matcher = Pattern.compile("^$s$").matcher(uri)
        if (matcher.find()) {
            return variables.mapIndexed { i, name -> name to matcher.group(i + 1) }.toMap()
        }
        return null
    }
}
