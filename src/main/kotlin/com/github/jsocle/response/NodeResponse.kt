package com.github.jsocle.response

import com.github.jsocle.html.Node
import java.io.PrintWriter

class NodeResponse(public val node: Node) : Response() {
    override fun invoke(printWriter: PrintWriter) {
        node.render(printWriter)
    }

    override val data: String
        get() = node.toString()
}


