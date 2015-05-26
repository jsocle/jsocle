package com.github.jsocle.requests

import com.github.jsocle.Blueprint
import com.github.jsocle.JSocle
import org.junit.Assert
import org.junit.Test

public class RequestHandlerTest {
    object bookFaqController : Blueprint() {
        val index = route("/") { -> }
    }

    object bookEtcController : Blueprint() {
        val isdnList = route("/isdn") { -> }
    }

    object bookController : Blueprint() {
        val list = route("/") { -> }

        init {
            register(bookFaqController, "/faq")
            register(bookEtcController)
        }
    }

    object etcController : Blueprint() {
        val faq = route("/faq") { -> }
    }

    object app : JSocle() {
        val index = route("/") { -> }

        init {
            register(bookController, urlPrefix = "/books")
            register(etcController)
        }
    }

    Test
    fun testUrl() {
        Assert.assertEquals("/", app.index.url())
    }

    Test
    fun testNestedUrl() {
        Assert.assertEquals("/books/", bookController.list.url())
        Assert.assertEquals("/books/faq/", bookFaqController.index.url())
    }

    Test
    fun testNestedWithoutPrefixUrl() {
        Assert.assertEquals("/faq", etcController.faq.url())
        Assert.assertEquals("/books/isdn", bookEtcController.isdnList.url())
    }
}
