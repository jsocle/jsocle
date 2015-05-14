package com.github.jsocle

import com.github.jsocle.client.TestClient
import com.github.jsocle.requests.RequestImpl
import com.github.jsocle.server.JettyServer
import com.github.jsocle.servlet.JSocleHttpServlet
import com.khtml.Node
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import java.net.URLDecoder
import java.nio.file.Path
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.properties.Delegates

public open class JSocle(staticPath: Path? = null) : JSocleApp() {
    public var server: JettyServer by Delegates.notNull()
        private set
    public val client: TestClient
        get() = TestClient(this)

    private val servlet = JSocleHttpServlet(this)
    private val rootPath = Paths.get(".").toAbsolutePath()
    private val staticPath = staticPath ?: rootPath.resolve("static")

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
        val result = findRequestHandler(URLDecoder.decode(req.getRequestURI(), "UTF-8"))
        if (result == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        val request = RequestImpl(result.pathVariables)
        com.github.jsocle.request.push(request)
        try {
            val response = result.handler.handle(request)
            resp.getWriter().use {
                when (response) {
                    is String -> it.print(response)
                    is Node -> response.render(it)
                    else -> throw IllegalArgumentException()
                }
            }
        } finally {
            com.github.jsocle.request.pop()
        }
    }
}