package com.example.firebaseapp;

public class SalaryEmployee {
    private String id;
    private String name;
    private String salary_by_hour;

    public SalaryEmployee(String id, String name, String salary_by_hour) {
        this.id = id;
        this.name = name;
        this.salary_by_hour = salary_by_hour;
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

    public String getSalary_by_hour() {
        return salary_by_hour;
    }

    public void setSalary_by_hour(String salary_by_hour) {
        this.salary_by_hour = salary_by_hour;
    }

    @Override
    public String toString() {
        return "SalaryEmployee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary_by_hour='" + salary_by_hour + '\'' +
                '}';
    }
}
