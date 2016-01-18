package com.github.jsocle.response

import java.io.PrintWriter
import javax.servlet.http.HttpServletResponse
import kotlin.collections.hashMapOf

abstract class Response(var statusCode: Int = HttpServletResponse.SC_OK) {
    abstract val data: String
    val headers: MutableMap<String, MutableList<String>> = hashMapOf()
    operator abstract fun invoke(printWriter: PrintWriter)
}