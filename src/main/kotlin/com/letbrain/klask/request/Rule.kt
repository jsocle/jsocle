package com.letbrain.klask.request

import java.util.regex.Pattern
import kotlin.properties.Delegates

public abstract class Rule(public val rule: String) {
    protected val re: Map<String, String> = mapOf("String" to "([^/]+)", "Int" to "([0-9]|[1-9][0-9]+)")
    protected val patternString: String by Delegates.lazy {
        rule.replaceAll("<([^>]+)>") { re[parseVariableName(it.group(1)).second]!! }
    }

    public val variables: Set<Variable> by Delegates.lazy {
        val matcher = Pattern.compile("<([^>]+)>").matcher(rule)
        val variables = linkedSetOf<Variable>()
        while (matcher.find()) {
            val pair = parseVariableName(matcher.group(1))
            variables.add(Variable.types[pair.second]!!(pair.first))
        }
        variables
    }

    public val variableNames: Set<String> by Delegates.lazy { variables.map { it.name }.toSet() }

    protected fun parseVariableName(rule: String): Pair<String, String> {
        return if (rule.contains(":")) {
            val pair = rule.split(":", 2)
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
