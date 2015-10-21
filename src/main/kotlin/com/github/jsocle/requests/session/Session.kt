package com.github.jsocle.requests.session

abstract class Session() {
    operator abstract fun get(name: String): Any
    operator abstract fun set(name: String, value: Any): Any
    operator abstract fun contains(name: String): Boolean
    abstract fun serialize(): String?
    abstract fun remove(key: String)
}
