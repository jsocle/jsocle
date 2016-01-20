package com.github.jsocle.response

import java.io.PrintWriter
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import kotlin.collections.arrayListOf
import kotlin.collections.hashMapOf

abstract class Response(var statusCode: Int = HttpServletResponse.SC_OK) {
    abstract val data: String
    val headers: MutableMap<String, MutableList<String>> = hashMapOf()
    var cookies: MutableList<Cookie> = arrayListOf()
    operator abstract fun invoke(printWriter: PrintWriter)
}