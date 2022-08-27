package com.stereo.ignitecacheserver.modal;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

public class Employee implements Serializable {

    @QuerySqlField(index = true)
    private int id;

    @QuerySqlField(index = true)
    private String name;

    @QuerySqlField(index = true)
    private String email;

    public Employee() {
    }

    public Employee(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
