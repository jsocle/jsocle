package com.letbrain.klask

import com.khtml.elements.Ul

public class IntegerationTest {
    object app : Klask() {
        val index = route("/") { ->
            return@route "Hello, World!"
        }

        val hello = route("/<name>") { name: String ->
            return@route "Hello, ${name}!"
        }

        val greet = route("/<name>/<many?:Int>") { name: String, many: Int ->
            return@route Ul {
                many.times { li("Hello, ${name}") }
            }
        }
    }
}
