package com.github.jsocle.server

import com.github.jsocle.client.Client
import com.github.jsocle.client.HttpClient
import org.eclipse.jetty.server.Server

public class JettyServer(public var port: Int) : Server(port) {
    val client: Client
        get() = HttpClient(port)
}