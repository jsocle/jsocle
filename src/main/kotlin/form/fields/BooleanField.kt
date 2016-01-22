package com.github.jsocle.form.fields

import com.github.jsocle.form.fieldMappers.BooleanMapper
import com.github.jsocle.form.SingleValueField
import com.github.jsocle.html.elements.Input

class BooleanField() : SingleValueField<Boolean, Input>(BooleanMapper()) {
    override fun render(): Input {
        return Input(name = name, type = "checkbox", value = "true", checked = if (value ?: false) "checked" else null)
    }
}