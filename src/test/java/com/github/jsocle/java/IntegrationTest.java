package com.github.jsocle.java;

import com.github.jsocle.JSocle;

import static com.github.jsocle.Request.request;

public class IntegrationTest {
    public static class App extends JSocle {
        public App() {
            route("/", () -> {
                return "Hello World!!!";
            });

            route("/<name>", name -> {
                return "Hello " + name;
            });

            route("/<name>/<times:Int>", (String name, Integer times) -> {
                String str = "";
                for (int i = 0; i < times; i++) {
                    str += "Hello " + name + "<br/>";
                }
                return str;
            });

            route("/<name>/test/<job>", (String name, String job) -> {
                return "Hello " + name + " : " + request.getPathVariables().get("job");
            });
        }
    }

    public static void main(String[] ar) {
        new App().run(8080, false);
    }
}
