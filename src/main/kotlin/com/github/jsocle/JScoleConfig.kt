package com.github.jsocle

import javax.crypto.spec.SecretKeySpec
import kotlin.properties.Delegates

public data class JScoleConfig(public var secretKey: String = "") {
    public val secretKeySpec: SecretKeySpec by Delegates.lazy {
        if (secretKey == "") {
            throw UnsupportedOperationException("JScoleConfig.secretKey is not defined.")
        }
        SecretKeySpec(secretKey.toByteArray("UTF-8"), "HmacSHA256");
    }
}