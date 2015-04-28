package com.letbrain.klask

import com.letbrain.klask.server.JettyServer
import com.letbrain.klask.servlet.KlaskHttpServlet
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import java.nio.file.Path
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.properties.Delegates

public open class Klask(staticPath: Path? = null) {
    public var server: JettyServer by Delegates.notNull()
        private set
    private val servlet = KlaskHttpServlet(this)
    private val rootPath = Paths.get(".").toAbsolutePath()
    private val staticPath = staticPath ?: rootPath.resolve("static")
    private val requestHandlers = arrayListOf<RequestHandler<*>>()

    public fun <R> route(rule: String, handler: () -> R) {
        requestHandlers.add(object : RequestHandler<R>(rule) {
            override fun handle(): R {
                return handler()
            }
        })
    }

    public fun <P1, R> route(rule: String, handler: (p1: P1) -> R) {
    }

    public fun <P1, P2, R> route(rule: String, handler: (p1: P1, p2: P2) -> R) {
    }

    public fun run(port: Int = 8080, onBackground: Boolean = false) {
        server = JettyServer(port)
        val servletContextHandler = ServletContextHandler()
        servletContextHandler.setContextPath("/")
        servletContextHandler.setResourceBase(staticPath.getParent().toString())
        servletContextHandler.addServlet(ServletHolder(DefaultServlet()), "/static/*")
        servletContextHandler.addServlet(ServletHolder(servlet), "/*")

        server.setHandler(servletContextHandler)
        server.start()
        if (!onBackground) {
            server.join()
        }
    }

    public fun stop() {
        server.stop()
    }

    fun processRequest(req: HttpServletRequest, resp: HttpServletResponse) {
        val requestURI = req.getRequestURI()
        val requestHandler = requestHandlers.firstOrNull { it.rule == requestURI }
        if (requestHandler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        val response = requestHandler.handle()
        resp.getWriter().use { it.print(response as String) }
    }
}