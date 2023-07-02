package com.example.firebaseapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee implements Serializable {
    private String id;
    private String name;
    private String role;
    private String salary_by_hour;
    private String total;
    private String created_at;
    public  Employee(String name,String total){
        this.name=name;
        this.total= total;
    }

    public Employee(String id, String name, String role, String salary_by_hour,String created_at) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary_by_hour = salary_by_hour;
        this.created_at =created_at;
    }
    public Employee(String id, String name, String role, String salary_by_hour) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary_by_hour = salary_by_hour;
        this.created_at =formattedDate;
    }

    public Employee() {

    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", salary_by_hour='" + salary_by_hour + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Employee(String name, String role, String salary_by_hour) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);
        this.name = name;
        this.role = role;
        this.salary_by_hour = salary_by_hour;
        this.created_at =formattedDate;
    }
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSalary_by_hour() {
        return salary_by_hour;
    }

    public void setSalary_by_hour(String salary_by_hour) {
        this.salary_by_hour = salary_by_hour;
    }
}
