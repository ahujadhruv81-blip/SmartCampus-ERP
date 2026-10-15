package com.smartcampus.erp.controller;

import com.smartcampus.erp.model.Student;
import com.smartcampus.erp.service.RiskLevel;
import com.smartcampus.erp.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

// Controller: REST Endpoints utilizing OOP, Collections, Exception Handling, and DAOs
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Exception Handling: calls service method throwing InvalidDataException
    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Student created = studentService.registerStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Collections & Generics: List<Student>
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Exception Handling: throws StudentNotFoundException when not found
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        boolean updated = studentService.updateStudent(id, student);
        return Map.of("success", updated, "studentId", id);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);
        return Map.of("success", deleted, "studentId", id);
    }

    // Collections & Generics: Set<String>
    @GetMapping("/departments")
    public Set<String> getUniqueDepartments() {
        return studentService.getUniqueDepartments();
    }

    // Collections & Generics: Map<RiskLevel, List<Student>>
    @GetMapping("/by-risk")
    public Map<RiskLevel, List<Student>> getStudentsByRisk() {
        return studentService.getStudentsGroupedByRisk();
    }

    // Collections & Generics: Generic filter method
    @GetMapping("/at-risk")
    public List<Student> getAtRiskStudents() {
        return studentService.getAtRiskStudents();
    }

    // Polymorphism & Interfaces: invoking generateReport on Reportable Student
    @GetMapping("/{id}/report")
    public Map<String, Object> getStudentReport(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return Map.of(
                "summary", studentService.generatePersonSummary(student),
                "reportText", studentService.generateReport(student),
                "reportData", student.getReportData()
        );
    }
}
