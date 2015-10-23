package com.github.jsocle.requests.session

import com.github.jsocle.JSocleConfig
import org.apache.commons.codec.digest.HmacUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

val String.urlEncoded: String
    get() {
        return URLEncoder.encode(this, "UTF-8")
    }

val String.decodeURL: String
    get() {
        return URLDecoder.decode(this, "UTF-8")
    }

val String.decodeBase64UrlSafe: ByteArray
    get() {
        return Base64.getUrlDecoder().decode(this)
    }

val ByteArray.encodeBase64UrlSafe: String
    get() {
        return Base64.getUrlEncoder().encodeToString(this)
    }


public class StringSession(cookie: String?, val config: JSocleConfig) : Session() {
    override fun remove(key: String) {
        map.remove(key)
    }

    private val map = deserialize(cookie) ?: hashMapOf()

    private fun deserialize(cookie: String?): MutableMap<String, String>? {
        val bytes = decode(cookie) ?: return null
        ByteArrayInputStream(bytes).use {
            InflaterInputStream(it).use {
                it.reader().use {
                    return it.readText()
                            .split('&')
                            .map { it.split('=', limit = 2) }
                            .filter { it.size == 2 }
                            .map {
                                val (key, value) = it.map { it.decodeURL }
                                key to value
                            }
                            .toMap() as MutableMap
                }
            }
        }
    }

    private fun decode(cookie: String?): ByteArray? {
        val pair = cookie?.split('.', limit = 2)
        if (pair?.size ?: 0 != 2) {
            return null
        }
        val (base64EncodedValue, mac) = pair
        try {
            val value = base64EncodedValue.decodeBase64UrlSafe
            return if (Arrays.equals(hashValue(value), mac.decodeBase64UrlSafe)) value else null
        } catch (e: IllegalArgumentException) {
            return null
        }
    }

    override fun contains(name: String): Boolean = name in map

    override fun set(name: String, value: Any) {
        if (value !is String) {
            throw UnsupportedOperationException("String Session only support string values")
        }
        // check secretKey is defined
        config.secretKey
        map[name] = value
    }

    override fun get(name: String): Any {
        return map[name] as String
    }

    override fun serialize(): String? {
        if (map.isEmpty()) {
            return null;
        }
        val value = ByteArrayOutputStream().use {
            DeflaterOutputStream(it).writer().use { writer ->
                map.forEach { writer.write("${it.key.urlEncoded}=${it.value.urlEncoded}") }
            }
            it
        }.toByteArray()
        return "${value.encodeBase64UrlSafe}.${hashValue(value).encodeBase64UrlSafe}"
    }

    private fun hashValue(value: ByteArray?) = HmacUtils.hmacSha256(config.secretKey, value)
}