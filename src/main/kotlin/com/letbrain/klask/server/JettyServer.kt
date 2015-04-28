package com.letbrain.klask.server

import com.letbrain.klask.client.Client
import com.letbrain.klask.client.HttpClient
import org.eclipse.jetty.server.Server

public class JettyServer(public var port: Int) : Server(port) {
    val client: Client
        get() = HttpClient(port)
}