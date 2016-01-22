package com.github.jsocle.form.fieldMappers

import com.github.jsocle.form.Field
import com.github.jsocle.form.FieldMapper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.isBlank

class DateFieldMapper(format: String) : FieldMapper<Date>() {
    private val sdf = SimpleDateFormat(format)

    override fun fromString(field: Field<Date, *>, string: String?): Date? {
        if (string?.isBlank() ?: true) {
            return null
        }
        try {
            return sdf.parse(string)
        } catch (e: ParseException) {
            field.errors.add(e.message!!)
        }
        return null
    }

    override fun toString(value: Date?): String? {
        if (value == null) {
            return null
        }

        return sdf.format(value)
    }
}