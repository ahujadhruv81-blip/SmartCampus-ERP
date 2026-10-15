package com.smartcampus.erp.model;

// OOP: Inheritance (Abstract Base Class)
public abstract class Person {
    private Long id;
    private String name;
    private String email;
    private String department;

    public Person() {
    }

    public Person(Long id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    // OOP: Polymorphism (Abstract Methods overridden by child classes)
    public abstract String getRole();
    public abstract String getSummary();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
