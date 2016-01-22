package com.github.jsocle.form.fields

import com.github.jsocle.form.fieldMappers.DateFieldMapper
import java.util.*

class DateField(format: String = "yyyy-MM-dd HH:mm:ss") : InputField<Date>(mapper = DateFieldMapper(format)) {
}
