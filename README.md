JSOCLE - Flask inspired
=======================

홈페이지 - http://jsocle.com

무엇에 쓰는 물건 인고?
-----------------

빠르고 정확하고 안전한 웹 어플리케이션을 만들기위한 Kotlin 으로 만들어진 마이크로 웹 프레임워크이다. 

Hello World
-----------

```kotlin
import com.github.jsocle.JSocle

object app : JSocle() {
    init {
        route("/") { -> "hello, world" }
    }
}

fun main(args: Array<String>) {
    app.run()
}
```

http://localhost:8080

Why?
----

* Kotlin Everywhere - 문자열, 설정파일, HTML 템플릿등을 모두 배제한다. kotlin 으로 프로그램을 완전하게 기술하도록 한다.
* IDE Everywhere - 모두 kotlin 이므로, IDE 지원이 최고로 강화된다.
* 단순한 설정 - 뻔한 내용을 굳이 설정해야 하는가?

Route
-----

기본

```kotlin
app.route("/") { -> "index" }
```

PathVariable

```kotlin
app.route("/<id:Int>") { id: Int -> "id = " + id }
```

ReverseRouter

route 의 url 메소드로 url을 생성할 수 있다.

```kotlin
val index = app.route("/") { -> }
val edit = app.route("/<id:Int>) { id: Int -> }
app.route("/showUrl") { -> index.url() + "," + edit.url(1) }
```

Nested Route

```kotlin
object postApp: Blueprint() {
    val list = route("/") { -> }
}

object app: JSocle() {
    init {
        register(postApp, "/post")
    }
}
```


Request
-------

RequestContext 내부에서는 request Singletone 으로 요청 관련 내용에 접근 할 수 있다.
```kotlin
import com.github.jsocle.request
request.parameters // query staring and post values
request.parameter("key") // access query string and post value
request.method // http method
request.handler // found request handler
request.handlerCallstack // call stack for found requeset handler
request.pathVariables // pathVariables
```

Request 배부에서 사용되는 데이터를 위한 g 가 있다. 아래 로그인 예쩨 참조
```kotlin
import com.github.jsocle.request

request.g["user"] = User()
```

Session
-------

```kotlin
import com.gtihub.jsocle.requset

request.session["userId"] = user.id
```

Test
----
test client 를 통해서 요청할 수 있다.
```kotlin
assert(app.client.get("/").data == "hello, world")
```

test client는 state 를 지원한다. app.client 를 사용할 경우 호출될때마다 생성한다.
```kotlin
val client = app.client
assert(client.get("/").data == "1")
assert(client.get("/").data == "2")
assert(app.client.get("/").data == "1")
```

Server 실행후 직접 호출 할 수 도 있다.
```kotlin
app.run {
    assert(app.server.client.get("/") == "hello, world")
}
```
JSocle --> Kotlin Everywhere
