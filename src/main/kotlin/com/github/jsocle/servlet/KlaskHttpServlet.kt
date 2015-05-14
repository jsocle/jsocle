package com.github.jsocle.servlet

import com.github.jsocle.Klask
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public class KlaskHttpServlet(private val app: Klask) : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        app.processRequest(req!!, resp!!)
    }
}