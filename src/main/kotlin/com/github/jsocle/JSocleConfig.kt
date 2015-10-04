package com.github.jsocle

public class JSocleConfig(secretKey: ByteArray = byteArrayOf()) {
    public var secretKey: ByteArray = secretKey
        get() {
            if (field.isEmpty()) {
                throw UnsupportedOperationException("JSocleConfig.secretKey is not defined.")
            }
            return field
        }
}