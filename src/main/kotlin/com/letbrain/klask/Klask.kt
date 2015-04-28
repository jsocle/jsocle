package com.letbrain.klask

import com.letbrain.klask.server.JettyServer
import com.letbrain.klask.servlet.KlaskHttpServlet
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.properties.Delegates

public open class Klask(staticPath: Path? = null) {
    public var server: JettyServer by Delegates.notNull()
        private set
    private val servlet = KlaskHttpServlet()
    private val rootPath = Paths.get(".").toAbsolutePath()
    private val staticPath = staticPath ?: rootPath.resolve("static")

    public fun <R> route(rule: String, handler: () -> R) {
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
}