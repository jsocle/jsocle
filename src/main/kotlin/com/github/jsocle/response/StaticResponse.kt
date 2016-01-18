package com.github.jsocle.response

import java.io.PrintWriter
import javax.servlet.http.HttpServletResponse
import kotlin.collections.hashMapOf

class StaticResponse(public override val data: String, headers: MutableMap<String, MutableList<String>> = hashMapOf(), statusCode: Int = HttpServletResponse.SC_OK) : Response(statusCode = statusCode) {
    init {
        this.headers.putAll(headers)
    }

    override fun invoke(printWriter: PrintWriter) {
        printWriter.print(data)
    }
}