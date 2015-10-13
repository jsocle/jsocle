package com.github.jsocle

class Hooks {
    val onTeardownRequestCallbacks = arrayListOf<() -> Unit>()
    var onBeforeFirstRequestCallbacks: MutableList<() -> Unit>? = arrayListOf()
}