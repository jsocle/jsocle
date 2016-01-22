package com.github.jsocle.form.fields

import com.github.jsocle.form.fieldMappers.StringFieldMapper

public class RadioStringField(choices: List<Pair<String, String>>) : RadioField<String>(choices, StringFieldMapper()) {
}