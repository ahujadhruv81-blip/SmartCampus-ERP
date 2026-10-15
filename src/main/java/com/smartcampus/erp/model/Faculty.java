package com.smartcampus.erp.model;

import java.util.LinkedHashMap;
import java.util.Map;

// OOP: Inheritance (Child Class), Interfaces (Reportable)
public class Faculty extends Person implements Reportable {
    private String employeeId;
    private String designation;
    private String specialization;

    public Faculty() {
        super();
    }

    public Faculty(Long id, String name, String email, String department,
                   String employeeId, String designation, String specialization) {
        super(id, name, email, department);
        this.employeeId = employeeId;
        this.designation = designation;
        this.specialization = specialization;
    }

    // OOP: Polymorphism (Method Overriding)
    @Override
    public String getRole() {
        return "FACULTY";
    }

    // OOP: Polymorphism (Method Overriding)
    @Override
    public String getSummary() {
        return String.format("Faculty: %s (EmpID: %s, Desig: %s, Dept: %s, Spec: %s)",
                getName(), employeeId, designation, getDepartment(), specialization);
    }

    // OOP: Interfaces (Implementation of Reportable)
    @Override
    public String generateReport() {
        return String.format("FACULTY PERFORMANCE REPORT | Name: %s | Employee ID: %s | Designation: %s | Specialization: %s",
                getName(), employeeId, designation, specialization);
    }

    // OOP: Interfaces (Implementation of Reportable)
    @Override
    public Map<String, Object> getReportData() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", getId());
        data.put("name", getName());
        data.put("email", getEmail());
        data.put("department", getDepartment());
        data.put("employeeId", employeeId);
        data.put("designation", designation);
        data.put("specialization", specialization);
        data.put("role", getRole());
        return data;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
