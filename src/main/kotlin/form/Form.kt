package com.github.jsocle.form

import kotlin.collections.*
import kotlin.reflect.KProperty
import kotlin.text.startsWith

public abstract class Form(parameters: Map<String, Array<String>>? = null,
                           val trim: Boolean = true) {
    val hasErrors: Boolean get() = errors.isNotEmpty()

    val fields: Array<Field<*, *>> by lazy(LazyThreadSafetyMode.NONE) {
        javaClass.methods
                .filter { it.name.startsWith("get") && it.name.length > 3 }
                .filter { Field::class.java.isAssignableFrom(it.returnType) }
                .map { it(this@Form) as Field<*, *> }
                .toTypedArray()
    }

    val errors: Array<String>
        get() = fields.flatMap { it.errors }.toTypedArray()

    public val parameters: Map<String, Array<String>>

    init {
        this.parameters = parameters ?: request.parameters()
    }

    operator protected fun <T : Field<*, *>> T.getValue(form: Form, propertyMetadata: KProperty<*>): T {
        initialize(form, propertyMetadata.name)
        return this
    }

    fun validateOnPost(): Boolean {
        if (request.method() != "POST") {
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
