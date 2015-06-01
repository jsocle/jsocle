package com.github.jsocle.servlet

import com.github.jsocle.JSocle
import com.github.jsocle.requests.Request
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