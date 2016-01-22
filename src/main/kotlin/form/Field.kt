package com.github.jsocle.form

import com.github.jsocle.html.Node
import kotlin.collections.*

abstract class Field<T : Any, N : Node>(protected val mapper: FieldMapper<T>, val defaults: List<T?>) {
    internal var _validators: Any? = null
    val hasErrors: Boolean get() = errors.isNotEmpty()
    val errors: MutableList<String> = arrayListOf()
    private var _values: List<T?> = listOf()
    var values: List<T?>
        get() = _values
        set(values: List<T?>) {
            rawFromValues = true
            _values = values
        }
    private var rawFromValues = false
    private var _raw: Array<String> = arrayOf()
    val raw: Array<String>
        get() {
            if (!rawFromValues) {
                return _raw
            }
            return values.map { mapper.toString(it) }.filter { it != null }.map { it!! }.toTypedArray()
        }

    var name: String = ""
        private set
    var form: Form? = null
        private set

    internal fun initialize(form: Form, name: String) {
        if (this.form == null) {
            this.form = form;
            this.name = name

            if (form.parameters.size == 0) {
                values = defaults
            } else {
                _raw = form.parameters[name] ?: arrayOf()
                _values = _raw.map { mapper.fromString(this, it) }.filter { it != null }
            }
        }
    }

    internal fun validate() {
        validators.forEach { it(this) }
    }

    abstract fun render(): N

    fun render(map: N.() -> Unit): N {
        val node = render()
        node.map()
        return node
    }
}

val <F : Field<*, *>> F.validators: MutableList<(F) -> Unit>
    get() {
        if (_validators == null) {
            _validators = arrayListOf<(F) -> Unit>()
        }
        @Suppress("UNCHECKED_CAST")
        return _validators as MutableList<(F) -> Unit>
    }
