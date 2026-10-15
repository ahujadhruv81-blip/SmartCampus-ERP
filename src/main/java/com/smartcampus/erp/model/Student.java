package com.smartcampus.erp.model;

import com.smartcampus.erp.service.RiskLevel;
import java.util.LinkedHashMap;
import java.util.Map;

// OOP: Inheritance (Child Class), Interfaces (Reportable)
public class Student extends Person implements Reportable {
    private String rollNumber;
    private double cgpa;
    private double attendancePercentage;
    private RiskLevel riskLevel;

    public Student() {
        super();
    }

    public Student(Long id, String name, String email, String department,
                   String rollNumber, double cgpa, double attendancePercentage, RiskLevel riskLevel) {
        super(id, name, email, department);
        this.rollNumber = rollNumber;
        this.cgpa = cgpa;
        this.attendancePercentage = attendancePercentage;
        this.riskLevel = riskLevel;
    }

    // OOP: Polymorphism (Method Overriding)
    @Override
    public String getRole() {
        return "STUDENT";
    }

    // OOP: Polymorphism (Method Overriding)
    @Override
    public String getSummary() {
        return String.format("Student: %s (Roll: %s, Dept: %s, CGPA: %.2f, Attendance: %.1f%%, Risk: %s)",
                getName(), rollNumber, getDepartment(), cgpa, attendancePercentage, riskLevel);
    }

    // OOP: Interfaces (Implementation of Reportable)
    @Override
    public String generateReport() {
        return String.format("ACADEMIC REPORT | Student: %s | Roll No: %s | CGPA: %.2f | Attendance: %.1f%% | Status: %s",
                getName(), rollNumber, cgpa, attendancePercentage, riskLevel);
    }

    // OOP: Interfaces (Implementation of Reportable)
    @Override
    public Map<String, Object> getReportData() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", getId());
        data.put("name", getName());
        data.put("email", getEmail());
        data.put("department", getDepartment());
        data.put("rollNumber", rollNumber);
        data.put("cgpa", cgpa);
        data.put("attendancePercentage", attendancePercentage);
        data.put("riskLevel", riskLevel != null ? riskLevel.name() : "UNKNOWN");
        data.put("role", getRole());
        return data;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }
}
