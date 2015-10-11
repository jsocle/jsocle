package com.github.jsocle

class Hooks {
    var onBeforeFirstRequestCallbacks: MutableList<() -> Unit>? = arrayListOf()
}