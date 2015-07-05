package com.github.jsocle

import javax.crypto.spec.SecretKeySpec
import kotlin.properties.Delegates

public data class JScoleConfig(public var secretKey: ByteArray = byteArrayOf()) {
    public val secretKeySpec: SecretKeySpec by Delegates.lazy {
        if (secretKey.isEmpty()) {
            throw UnsupportedOperationException("JScoleConfig.secretKey is not defined.")
        }
        SecretKeySpec(secretKey, "HmacSHA256");
    }
}