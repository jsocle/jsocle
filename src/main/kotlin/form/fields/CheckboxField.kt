package com.github.jsocle.form.fields

import com.github.jsocle.form.Field
import com.github.jsocle.form.FieldMapper
import com.github.jsocle.html.elements.Ul
import kotlin.collections.forEach
import kotlin.collections.listOf

open class CheckboxField<T : Any>(var choices: List<Pair<T, String>> = listOf(), mapper: FieldMapper<T>, defaults: List<T> = listOf())
: Field<T, Ul>(mapper, defaults) {

    override fun render(): Ul {
        return Ul(class_ = "jsocle-form-field-checkbox") {
            choices.forEach {
                li {
                    label {
                        input(type = "checkbox", name = name, value = mapper.toString(it.first), checked = if (it.first in this@CheckboxField.values) "checked" else null)
                        +it.second
                    }
                }
            }
        }
    }
}