package com.github.jsocle.form.fields

import com.github.jsocle.form.FieldMapper
import com.github.jsocle.form.SingleValueField
import com.github.jsocle.html.elements.Select
import kotlin.collections.forEach

open class SelectField<T : Any>(var choices: List<Pair<T, String>>, mapper: FieldMapper<T>, default: T? = null)
: SingleValueField<T, Select>(mapper, default) {
    override fun render(): Select {
        return Select(name = name) {
            choices.forEach {
                option(
                        value = mapper.toString(it.first), text_ = it.second,
                        selected = if (it.first == value) "selected" else null
                )
            }
        }
    }
}