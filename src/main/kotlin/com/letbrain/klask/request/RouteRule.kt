package com.letbrain.klask.request

import java.util.regex.Pattern
import kotlin.properties.Delegates

public class RouteRule(rule: String) : Rule(rule) {
    private val pattern: Pattern by Delegates.lazy {
        Pattern.compile("^$patternString$")
    }

    public fun match(uri: String): Map<String, Any>? {
        val matcher = pattern.matcher(uri)
        if (matcher.find()) {
            return variables.mapIndexed { i, variable -> variable.name to variable.parse(matcher.group(i + 1)) }.toMap()
        }
        return null
    }
}