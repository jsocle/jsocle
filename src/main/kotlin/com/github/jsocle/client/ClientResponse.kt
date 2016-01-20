package com.github.jsocle.client

data class ClientResponse(val data: String, val headers: MutableMap<String, MutableList<String>>, val url: String, val statusCode: Int)