package com.github.jsocle.form

import com.github.jsocle.request
import com.github.jsocle.requests.Request
import kotlin.reflect.KProperty

public abstract class Form(parameters: Map<String, List<String>>? = null,
                           val trim: Boolean = true) {
    val hasErrors: Boolean get() = errors.isNotEmpty()

    val fields: Array<Field<*, *>> by lazy(LazyThreadSafetyMode.NONE) {
        javaClass.methods
                .filter { it.name.startsWith("get") && it.name.length > 3 }
                .filter { Field::class.java.isAssignableFrom(it.returnType) }
                .map { it(this@Form) as Field<*, *> }
                .toTypedArray()
    }

    val errors: Map<String, List<String>>
        get() = fields.filter { it.hasErrors }.map { it.name to it.errors }.toMap()

    public val parameters: Map<String, List<String>>

    init {
        this.parameters = parameters ?: request.parameters
    }

    operator protected fun <T : Field<*, *>> T.getValue(form: Form, propertyMetadata: KProperty<*>): T {
        initialize(form, propertyMetadata.name)
        return this
    }

    fun validateOnPost(): Boolean {
        if (request.method != Request.Method.POST) {
            return false
        }
        return validate()
    }

    open fun validate(): Boolean {
        fields.forEach { it.validate() }
        return !hasErrors
    }
}

fun <E> MutableCollection<E>.add(vararg items: E) {
    addAll(items.toList())
}
