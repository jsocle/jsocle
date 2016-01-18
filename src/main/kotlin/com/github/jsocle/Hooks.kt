package com.github.jsocle

import kotlin.collections.arrayListOf

class Hooks {
    val onTeardownRequestCallbacks = arrayListOf<() -> Unit>()
    var onBeforeFirstRequestCallbacks: MutableList<() -> Unit>? = arrayListOf()
}