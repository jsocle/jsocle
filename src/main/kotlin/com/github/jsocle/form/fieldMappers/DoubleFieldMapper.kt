package com.github.jsocle.form.fieldMappers

import com.github.jsocle.form.Field
import com.github.jsocle.form.FieldMapper

public class DoubleFieldMapper : FieldMapper<Double>() {
    override fun toString(value: Double?): String? = value?.toString()

    override fun fromString(field: Field<Double, *>, string: String?): Double? {
        return try {
            string?.toDouble()
        } catch(e: NumberFormatException) {
            field.errors.add("Not a valid double value")
            null
        }
    }
}