package com.github.jsocle.form.test

import com.github.jsocle.JSocle
import com.github.jsocle.requests.Request

private object app : JSocle() {
    val index = route("/") { -> }
}

public fun parameters(vararg parameters: Pair<String, String>, method: Request.Method = Request.Method.GET, body: () -> Unit) {
    app.client.get(app.index.url(*parameters), method, body)
}
