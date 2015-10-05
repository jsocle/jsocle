package com.github.jsocle.response

import java.io.PrintWriter

class StaticResponse(public override val data: String, headers: MutableMap<String, MutableList<String>> = hashMapOf()) : Response() {
    init {
        this.headers.putAll(headers)
    }

    override fun invoke(printWriter: PrintWriter) {
        printWriter.print(data)
    }
}