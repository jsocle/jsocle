package com.github.jsocle.requests

import java.util.regex.Pattern
import kotlin.collections.*
import kotlin.text.*

private val VARIABLE_MAP = mapOf("String" to "([^/]+)", "Int" to "([0-9]|[1-9][0-9]+)")
private val variablePattenString = "<([^>]+)>"
private val variablePattern = Pattern.compile(variablePattenString)

public abstract class Rule(public val rule: String) {
    protected val patternString: String = rule.replace(variablePattenString.toRegex()) {
        VARIABLE_MAP[parseVariableName(it.groups[1]!!.value).second]!!
    }

    public val variables: Set<Variable> = run {
        val matcher = variablePattern.matcher(rule)
        val variables = linkedSetOf<Variable>()
        while (matcher.find()) {
            val pair = parseVariableName(matcher.group(1))
            variables.add(Variable.types[pair.second]!!(pair.first))
        }
        variables
    }

    public val variableNames: Set<String> = variables.map { it.name }.toSet()
    public val variableNameList: List<String> = variableNames.toList()


    protected fun parseVariableName(rule: String): Pair<String, String> {
        return if (rule.contains(":")) {
            val pair = rule.split(":".toRegex(), 2)
            return pair[0] to pair[1]
        } else {
            rule to "String"
        }
    }

    public abstract class Variable(public val name: String) {
        public abstract fun parse(value: String): Any

        companion object {
            public val types: MutableMap<String, (name: String) -> Variable> = hashMapOf(
                    "String" to ::StringVariable, "Int" to :: IntVariable
            )
        }
    }

    public class StringVariable(name: String) : Variable(name) {
        override fun parse(value: String): Any {
            return value
        }
    }

    public class IntVariable(name: String) : Variable(name) {
        override fun parse(value: String): Any {
            return value.toInt()
        }
    }
}
