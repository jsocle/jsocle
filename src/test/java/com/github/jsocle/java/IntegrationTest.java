package com.github.jsocle.java;

import com.github.jsocle.JSocle;
import com.github.jsocle.request;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    public static void main(String[] ar) {
        new App().run(8080, false);
    }

    @Test
    public void testApp() {
        App app = new App();
        assertEquals("Hello World!!!", app.getClient().get("/").getData());
        assertEquals("Hello John", app.getClient().get("/John").getData());
        assertEquals("Hello John<br/>Hello John<br/>Hello John<br/>", app.getClient().get("/John/3").getData());
        assertEquals("Hello John : Cook", app.getClient().get("/John/job/Cook").getData());
    }

    public static class App extends JSocle {
        public App() {
            route("/", () -> "Hello World!!!");

            route("/<name>", name -> "Hello " + name);

            route("/<name>/<times:Int>", (String name, Integer times) -> {
                String str = "";
                for (int i = 0; i < times; i++) {
                    str += "Hello " + name + "<br/>";
                }
                return str;
            });

            route(
                    "/<name>/job/<job>",
                    (String name, String job) -> "Hello " + name + " : " + request.getPathVariables().get("job")
            );
        }
    }
}
