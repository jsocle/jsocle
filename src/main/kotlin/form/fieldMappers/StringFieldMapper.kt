package com.github.jsocle.form.fieldMappers

import com.github.jsocle.form.Field
import com.github.jsocle.form.FieldMapper
import kotlin.text.trim

public class StringFieldMapper : FieldMapper<String>() {
    override fun toString(value: String?): String? = value

    override fun fromString(field: Field<String, *>, string: String?): String? =
            if (string != null && field.form!!.trim) string.trim() else string
}