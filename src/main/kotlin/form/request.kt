package com.github.jsocle.form

object request {
    var parameters: () -> Map<String, Array<String>> = {
        throw IllegalArgumentException("com.github.jsocle.form.request parameters was not set.")
    }

    var method: () -> String = {
        throw IllegalArgumentException("com.github.jsocle.form.request method was not set.")
    }
}