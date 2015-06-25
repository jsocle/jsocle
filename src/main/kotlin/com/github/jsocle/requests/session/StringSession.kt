package com.github.jsocle.requests.session

import com.github.jsocle.JScoleConfig
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream
import javax.servlet.http.Cookie

val String.urlEncoded: String
    get() {
        return URLEncoder.encode(this, "UTF-8")
    }

val String.decodeURL: String
    get() {
        return URLDecoder.decode(this, "UTF-8")
    }

public class StringSession(cookie: String?, val config: JScoleConfig) : Session() {
    private val map = deserialize(cookie) ?: hashMapOf()

    private fun deserialize(cookie: String?): MutableMap<String, String>? {
        val bytes = decode(cookie) ?: return null
        ByteArrayInputStream(bytes).use {
            InflaterInputStream(it).use {
                it.reader()use {
                    return it.readText()
                            .split('&')
                            .map { it.split('=', limit = 2) }
                            .filter { it.size() == 2 }
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
        if (cookie == null) {
            return null
        }

        try {
            return Base64.getUrlDecoder().decode(cookie)
        } catch (e: IllegalArgumentException) {
            return null
        }
    }

    override fun contains(name: String): Boolean = name in map

    override fun set(name: String, value: Any) {
        config.secretKeySpec
        if (value !is String) {
            throw UnsupportedOperationException("String Session only support string values")
        }
        map[name] = value
    }

    override fun get(name: String): Any {
        return map[name] as String
    }

    override fun serialize(): String {
        val compressed = ByteArrayOutputStream().use {
            DeflaterOutputStream(it).writer().use { writer ->
                map.forEach { writer.write("${it.key.urlEncoded}=${it.value.urlEncoded}") }
            }
            it
        }.toByteArray()
        return Base64.getUrlEncoder().encodeToString(compressed)!!
    }
}