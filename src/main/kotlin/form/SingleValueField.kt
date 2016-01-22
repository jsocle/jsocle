package com.github.jsocle.form

import com.github.jsocle.html.Node
import kotlin.collections.first
import kotlin.collections.listOf

abstract class SingleValueField<T : Any, N : Node>(mapper: FieldMapper<T>, val default: T? = null) :
        Field<T, N>(mapper, default?.toList() ?: listOf()) {
    var value: T?
        get() {
            // values.firstOrNull() dose not work
            if (values.size == 0) {
                return null
            }
            return values.first()
        }
        set(value: T?) {
            values = value?.toList() ?: listOf()
        }
}

private fun <T : Any?> T.toList(): List<T> {
    return if (this == null) listOf() else listOf(this)
}