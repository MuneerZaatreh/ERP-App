package com.example.firebaseapp;

import java.io.Serializable;

public class Project   implements Serializable {
    private String id;
    private String name;
    private String customer_id;
    private String customer_name;
    private String location;
    private String created_at;

    public Project(String id, String name, String customer_id, String location,String created_at) {
        this.id = id;
        this.name = name;
        this.customer_id = customer_id;
        this.location = location;
        this.created_at = created_at;
    }
    public Project(String name, String customer_id, String location,String created_at) {
        this.name = name;
        this.customer_id = customer_id;
        this.location = location;
        this.created_at = created_at;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", location='" + location + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
