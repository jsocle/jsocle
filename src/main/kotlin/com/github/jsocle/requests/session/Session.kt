package com.github.jsocle.requests.session

import javax.servlet.http.Cookie

abstract class Session() {
    public abstract fun get(name: String): Any
    public abstract fun set(name: String, value: Any): Any
    public abstract fun contains(name: String): Boolean
    public abstract fun serialize(): String
}
