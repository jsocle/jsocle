package com.github.jsocle

import com.github.jsocle.requests.PrefixRule
import com.github.jsocle.requests.RequestHandler
import com.github.jsocle.requests.RequestHandlerMatchResult
import com.github.jsocle.requests.RequestImpl
import com.github.jsocle.requests.handlers.RequestHandler0
import com.github.jsocle.requests.handlers.RequestHandler1
import java.util.ArrayList

public abstract class JSocleApp {
    protected val requestHandlers: ArrayList<RequestHandler<*>> = arrayListOf()
    protected val children: ArrayList<Bridge> = arrayListOf()

    public fun <R> route(rule: String, handler: () -> R): RequestHandler0<R> {
        val rh = RequestHandler0(this, rule, handler)
        requestHandlers.add(rh)
        return rh
    }

    public fun <P1, R> route(rule: String, handler: (p1: P1) -> R): RequestHandler1<R, P1> {
        val rh = RequestHandler1(this, rule, handler)
        requestHandlers.add(rh)
        return rh
    }

    public fun <P1, P2, R> route(rule: String, handler: (p1: P1, p2: P2) -> R): RequestHandler<R> {
        val rh = object : RequestHandler<R>(this, rule) {
            suppress("UNCHECKED_CAST")
            override fun handle(request: RequestImpl): R {
                val variableNames = this.rule.variableNameList
                val p1 = request.pathVariables[variableNames[0]] as P1
                val p2 = request.pathVariables[variableNames[1]] as P2
                return handler(p1, p2)
            }
        }
        requestHandlers.add(rh);
        return rh
    }

    public fun <T : Blueprint> register(app: T, urlPrefix: String? = null): T {
        children.add(Bridge(app, this, urlPrefix))
        return app
    }


    public fun findRequestHandler(uri: String): RequestHandlerMatchResult? {
        val matchResult = requestHandlers.firstMapNotNull {
            val pathVariables = it.rule.match(uri)
            if (pathVariables != null) RequestHandlerMatchResult(it, pathVariables) else null
        }
        if (matchResult != null) {
            return matchResult
        }
        return children.firstMapNotNull { findChildRequestHandler(it, uri) }
    }

    protected fun findChildRequestHandler(child: Bridge, uri: String): RequestHandlerMatchResult? {
        val rule = child.rule
        if (rule != null) {
            val result = rule.match(uri)
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

    public class Bridge(public val app: Blueprint, public val parent: JSocleApp, urlPrefix: String?) {
        public val rule: PrefixRule? = if (urlPrefix != null) PrefixRule(urlPrefix) else null
        init {
            app.bridge = this
        }
    }
}
