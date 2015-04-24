package com.letbrain.klask

public open class Klask {
    public var initialized: Boolean = false
        private set

    public fun init() {
        if (initialized) {
            throw ExceptionInInitializerError("Already initialized.")
        }
        initialized = true
    }
}