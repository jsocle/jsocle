package com.github.jsocle.form.fields

import com.github.jsocle.html.elements.Input

public class PasswordField() : StringField() {
    override fun render(): Input {
        return Input(name = name, type = "password")
    }
}