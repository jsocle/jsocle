package com.github.jsocle.form.test

import com.github.jsocle.form.request

object testRequest {
    val localParameters = ThreadLocal<Map<String, Array<String>>>()
    val localMethod = ThreadLocal<String>()

    var parameters: Map<String, Array<String>>
        get() = localParameters.get()
        set(parameters: Map<String, Array<String>>) {
            localParameters.set(parameters)
            request.parameters = { this.parameters }
        }

    var method: String
        get() = localMethod.get()
        set(method: String) {
            localMethod.set(method)
            request.method = { this.method }
        }
}

public @SafeVarargs fun parameters(vararg parameters: Pair<String, String>, method: String = "GET") {
    testRequest.parameters = parameters.groupBy { it.first }.mapValues { it.value.map { it.second }.toTypedArray() }
    testRequest.method = method
}
