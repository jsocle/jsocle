package com.github.jsocle

import kotlin.collections.isEmpty

open class JSocleConfig(secretKey: ByteArray = byteArrayOf(), val debug: Boolean = false) {
    var secretKey: ByteArray = secretKey
        get() {
            if (field.isEmpty()) {
                throw UnsupportedOperationException("JSocleConfig.secretKey is not defined.")
            }
            return field
        }
}