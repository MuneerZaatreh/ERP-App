package com.example.firebaseapp;

import java.io.Serializable;

public class Customer implements Serializable {


    private String id;
    private String name;
    private String age;
    private String url;
    private String created_at;

    public Customer(String id,String name, String age, String created_at,String url) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.created_at = created_at;
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String toString() {
        return "Customer{"
                +"id:" + id
                +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
