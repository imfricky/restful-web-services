package com.courses.rest.webservices.restfulwebservices.helloworld;

public class HelloWorldBean {
    private  String message;

    public HelloWorldBean(String hello_world) {
        this.message = hello_world;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
