package com.github.jsocle.form.fields

import com.github.jsocle.form.fieldMappers.IntFieldMapper

public class IntField(default: Int? = null) : InputField<Int>(IntFieldMapper(), default)