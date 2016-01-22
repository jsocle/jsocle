package com.github.jsocle.form.fieldMappers

import com.github.jsocle.form.Field
import com.github.jsocle.form.FieldMapper
import kotlin.text.toInt

public class IntFieldMapper : FieldMapper<Int>() {
    override fun toString(value: Int?): String? = value?.toString()

    override fun fromString(field: Field<Int, *>, string: String?): Int? {
        return try {
            string?.toInt()
        } catch(e: NumberFormatException) {
            field.errors.add("Not a valid integer value")
            null
        }
    }
}