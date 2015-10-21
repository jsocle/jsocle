package com.github.jsocle

open class JSocleConfig(secretKey: ByteArray = byteArrayOf()) {
    var secretKey: ByteArray = secretKey
        get() {
            if (field.isEmpty()) {
                throw UnsupportedOperationException("JSocleConfig.secretKey is not defined.")
            }
            return field
        }
}