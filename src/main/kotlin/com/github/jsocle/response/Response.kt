package com.github.jsocle.response

import java.io.PrintWriter

abstract class Response() {
    abstract val data: String
    val headers: MutableMap<String, MutableList<String>> = hashMapOf()
    operator abstract fun invoke(printWriter: PrintWriter)
}