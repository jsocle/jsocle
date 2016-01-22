package com.github.jsocle.form.fields

import com.github.jsocle.form.fieldMappers.DoubleFieldMapper

public class DoubleField(default: Double? = null) : InputField<Double>(DoubleFieldMapper(), default)