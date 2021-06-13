package com.courses.rest.webservices.restfulwebservices.versioning;

public class PersonV1 {
    private String Name;

    public PersonV1(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
