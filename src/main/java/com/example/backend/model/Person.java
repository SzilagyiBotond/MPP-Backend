package com.example.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "person")
public class Person {

    @SequenceGenerator(
            name = "person_sequence",
            sequenceName = "person_sequence",
            allocationSize = 1
    )
    private @Id @GeneratedValue Long id;
    private String name;
    private String status;
    private String revolutId;


    public Person(){

    }

    public Person(String name, String status, String revolutId) {
        this.name = name;
        this.status = status;
        this.revolutId = revolutId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRevolutId() {
        return revolutId;
    }

    public void setRevolutId(String revolutId) {
        this.revolutId = revolutId;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", revolutId='" + revolutId + '\'' +
                '}';
    }
}
