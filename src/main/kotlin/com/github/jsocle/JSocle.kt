package com.github.jsocle

import com.github.jsocle.client.TestClient
import com.github.jsocle.html.Node
import com.github.jsocle.requests.Request
import com.github.jsocle.requests.RequestHandlerMatchResult
import com.github.jsocle.requests.RequestImpl
import com.github.jsocle.requests.session.Session
import com.github.jsocle.requests.session.StringSession
import com.github.jsocle.server.JettyServer
import com.github.jsocle.servlet.JSocleHttpServlet
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import java.net.URLDecoder
import java.nio.file.Path
import java.nio.file.Paths
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.properties.Delegates

public open class JSocle(config: JScoleConfig? = null, staticPath: Path? = null) : JSocleApp() {
    public var server: JettyServer by Delegates.notNull()
        private set
    public val client: TestClient
        get() = TestClient(this)

    public val config: JScoleConfig = config ?: JScoleConfig()

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

    fun requestContext(req: HttpServletRequest, method: Request.Method, body: (request: RequestImpl?, result: RequestHandlerMatchResult?) -> Unit) {
        val requestUri = URLDecoder.decode(req.getRequestURI(), "UTF-8")
        val result = findRequestHandler(requestUri)
        if (result == null) {
            body(null, null)
            return
        }
        val request = RequestImpl(requestUri, result.pathVariables, req, method, this)
        com.github.jsocle.request.push(request)
        try {
            body(request, result)
        } finally {
            com.github.jsocle.request.pop()
        }
    }

    fun processRequest(req: HttpServletRequest, resp: HttpServletResponse, method: Request.Method) {
        requestContext(req, method) { request, result ->
            if (request == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND)
                return@requestContext
            }
            val response = result!!.handler.handle(request)
            resp.addCookie(Cookie("session", request.session.serialize()));
            resp.getWriter().use {
                when (response) {
                    is String -> it.print(response)
                    is Node -> response.render(it)
                    is Unit -> it.print("")
                    else -> throw IllegalArgumentException()
                }
            }

        }
    }

    public fun buildSession(cookie: String?): Session {
        return StringSession(cookie, config)
    }
}