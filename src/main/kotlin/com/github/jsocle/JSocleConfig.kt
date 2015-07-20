package com.github.jsocle

public data class JSocleConfig(secretKey: ByteArray = byteArrayOf()) {
    public var secretKey: ByteArray = secretKey
        get() {
            if ($secretKey.isEmpty()) {
                throw UnsupportedOperationException("JSocleConfig.secretKey is not defined.")
            }
            return $secretKey
        }
}