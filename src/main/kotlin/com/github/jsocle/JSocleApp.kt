package com.github.jsocle

import com.github.jsocle.requests.PrefixRule
import com.github.jsocle.requests.RequestHandler
import com.github.jsocle.requests.RequestHandlerMatchResult
import com.github.jsocle.requests.RequestImpl
import java.util.ArrayList

public abstract class JSocleApp {
    protected val requestHandlers: ArrayList<RequestHandler<*>> = arrayListOf()
    protected val children: ArrayList<Child> = arrayListOf()

    public fun <R> route(rule: String, handler: () -> R) {
        requestHandlers.add(object : RequestHandler<R>(rule) {
            override fun handle(request: RequestImpl): R {
                return handler()
            }
        })
    }

    public fun <P1, R> route(rule: String, handler: (p1: P1) -> R) {
        requestHandlers.add(object : RequestHandler<R>(rule) {
            override fun handle(request: RequestImpl): R {
                val p1 = (request.pathVariables[this.rule.variableNames.first()]) as P1
                return handler(p1)
            }
        })
    }

    public fun <P1, P2, R> route(rule: String, handler: (p1: P1, p2: P2) -> R) {
        requestHandlers.add(object : RequestHandler<R>(rule) {
            override fun handle(request: RequestImpl): R {
                val variableNames = this.rule.variableNames.toList()
                val p1 = request.pathVariables[variableNames[0]] as P1
                val p2 = request.pathVariables[variableNames[1]] as P2
                return handler(p1, p2)
            }
        });
    }

    public fun register(app: JSocleApp, urlPrefix: String? = null) {
        children.add(Child(app, urlPrefix))
    }


    protected fun findRequestHandler(uri: String): RequestHandlerMatchResult? {
        val matchResult = requestHandlers.firstMapNotNull {
            val pathVariables = it.rule.match(uri)
            if (pathVariables != null) RequestHandlerMatchResult(it, pathVariables) else null
        }
        if (matchResult != null) {
            return matchResult
        }
        return children.firstMapNotNull { findChildRequestHandler(it, uri) }
    }

    private fun findChildRequestHandler(child: Child, uri: String): RequestHandlerMatchResult? {
        if (child.rule != null) {
            val result = child.rule.match(uri)
            if (result == null) {
                return null;
            }

            val handlerResult = child.app.findRequestHandler(result.uri)
            if (handlerResult != null) {
                return RequestHandlerMatchResult(
                        handlerResult.handler, result.pathVariables + handlerResult.pathVariables
                );
            }
            return null;
        }
        return child.app.findRequestHandler(uri)
    }

    public class Child(public val app: JSocleApp, public val urlPrefix: String?) {
        public val rule: PrefixRule? = if (urlPrefix != null) PrefixRule(urlPrefix) else null
    }
}
