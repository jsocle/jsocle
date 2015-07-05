package com.github.jsocle

public data class JScoleConfig(secretKey: ByteArray = byteArrayOf()) {
    public var secretKey: ByteArray = secretKey
        get() {
            if ($secretKey.isEmpty()) {
                throw UnsupportedOperationException("JScoleConfig.secretKey is not defined.")
            }
            return $secretKey
        }
}