package com.letbrain.klask

import com.khtml.Node
import com.letbrain.klask.client.TestClient
import com.letbrain.klask.request.RequestHandler
import com.letbrain.klask.request.RequestHandlerMatchResult
import com.letbrain.klask.request.RequestImpl
import com.letbrain.klask.server.JettyServer
import com.letbrain.klask.servlet.KlaskHttpServlet
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import java.net.URLDecoder
import java.nio.file.Path
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.properties.Delegates

public open class Klask(staticPath: Path? = null) {
    public var server: JettyServer by Delegates.notNull()
        private set
    public val client: TestClient
        get() = TestClient(this)

    private val servlet = KlaskHttpServlet(this)
    private val rootPath = Paths.get(".").toAbsolutePath()
    private val staticPath = staticPath ?: rootPath.resolve("static")
    private val requestHandlers = arrayListOf<RequestHandler<*>>()

    public fun <R> route(rule: String, handler: () -> R) {
        requestHandlers.add(object : RequestHandler<R>(rule) {
            override fun handle(request: RequestImpl): R {
                return handler()
            }
        })
    }

    public fun <P1, R> route(rule: String, handler: (p1: P1) -> R) {
        requestHandlers.add(object : RequestHandler<R>(rule) {
            override fun handle(request: RequestImpl): R {
                val p1 = (request.pathVariables[this.rule.variableNames.first()]) as P1
                return handler(p1)
            }
        })
    }

    public fun <P1, P2, R> route(rule: String, handler: (p1: P1, p2: P2) -> R) {
        requestHandlers.add(object : RequestHandler<R>(rule) {
            override fun handle(request: RequestImpl): R {
                val variableNames = this.rule.variableNames.toList()
                val p1 = request.pathVariables[variableNames[0]] as P1
                val p2 = request.pathVariables[variableNames[1]] as P2
                return handler(p1, p2)
            }
        });
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

    private fun findRequestHandler(uri: String): RequestHandlerMatchResult? {
        for (handler in requestHandlers) {
            val pathVariables = handler.rule.match(uri)
            if (pathVariables != null) {
                return RequestHandlerMatchResult(handler, pathVariables)
            }
        }
        return null
    }

    fun processRequest(req: HttpServletRequest, resp: HttpServletResponse) {
        val result = findRequestHandler(URLDecoder.decode(req.getRequestURI(), "UTF-8"))
        if (result == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        val request = RequestImpl(result.pathVariables)
        val response = result.handler.handle(request)
        resp.getWriter().use {
            when (response) {
                is String -> it.print(response)
                is Node -> response.render(it)
                else -> throw IllegalArgumentException()
            }
        }
    }
}