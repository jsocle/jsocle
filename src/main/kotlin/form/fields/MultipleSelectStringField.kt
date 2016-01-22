package com.github.jsocle.form.fields

import com.github.jsocle.form.fieldMappers.StringFieldMapper
import kotlin.collections.listOf

public class MultipleSelectStringField(choices: List<Pair<String, String>>, defaults: List<String> = listOf())
: MultipleSelectField<String>(choices, defaults, StringFieldMapper()) {
}