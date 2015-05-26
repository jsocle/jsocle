package com.github.jsocle

import kotlin.properties.Delegates

abstract public class Blueprint() : JSocleApp() {
    var bridge: JSocleApp.Bridge by Delegates.notNull()
    public val urlPrefixes: String by Delegates.lazy {
        val parent = bridge.parent
        if (parent is Blueprint) {
            parent.urlPrefixes + (bridge.rule?.rule ?: "")
        } else {
            bridge.rule?.rule ?: ""
        }
    }
}