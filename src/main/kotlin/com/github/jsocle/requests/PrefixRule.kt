package com.github.jsocle.requests

import java.util.regex.Pattern

public class PrefixRule(rule: String) : Rule(rule) {
    private val pattern by lazy(LazyThreadSafetyMode.NONE) { Pattern.compile("^$patternString") }

    public fun match(uri: String): MatchResult? {
        val matcher = pattern.matcher(uri)
        if (matcher.find()) {
            return MatchResult(
                    variables.mapIndexed { i, variable -> variable.name to variable.parse(matcher.group(i + 1)) }
                            .toMap(),
                    matcher.replaceAll("")
            )
        }
        return null
    }

    public data class MatchResult(public val pathVariables: Map<String, Any>, public val uri: String)
}