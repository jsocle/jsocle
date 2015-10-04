package com.github.jsocle.requests.session

abstract class Session() {
    operator public abstract fun get(name: String): Any
    operator public abstract fun set(name: String, value: Any): Any
    operator public abstract fun contains(name: String): Boolean
    public abstract fun serialize(): String?
}
