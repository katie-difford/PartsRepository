package com.katehdiffo.parts;

import ro.pippo.core.Application;

public class PartApplication extends Application {

    @Override
    protected void onInit() {
        GET("/", routeContext -> {
            routeContext.json().send(new Blah("kateh", 22));
        });
    }

    public static class Blah {
        private final String name;
        private final int age;

        public Blah(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
