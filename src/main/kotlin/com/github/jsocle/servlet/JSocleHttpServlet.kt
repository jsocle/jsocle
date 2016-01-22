package com.github.jsocle.servlet

import com.github.jsocle.JSocle
import com.github.jsocle.requests.Request
import org.eclipse.jetty.server.handler.ErrorHandler
import java.io.Writer
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public class JSocleHttpServlet(private val app: JSocle) : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        app.processRequest(req!!, resp!!, Request.Method.GET)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        app.processRequest(req!!, resp!!, Request.Method.POST)
    }
}

public class JsocleErrorPageHandler(private val debug: Boolean) : ErrorHandler() {
    override fun writeErrorPage(request: HttpServletRequest?, writer: Writer?, code: Int, message: String?, showStacks: Boolean) {
        super.writeErrorPage(request, writer, code, if (debug) null else message, showStacks && debug)
    }
}