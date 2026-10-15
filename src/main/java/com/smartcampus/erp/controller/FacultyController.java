package com.smartcampus.erp.controller;

import com.smartcampus.erp.dao.FacultyDAO;
import com.smartcampus.erp.exception.InvalidDataException;
import com.smartcampus.erp.exception.StudentNotFoundException;
import com.smartcampus.erp.model.Faculty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controller: REST Endpoints utilizing Faculty DAO, Polymorphism, and Interfaces
@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    private final FacultyDAO facultyDAO;

    public FacultyController(FacultyDAO facultyDAO) {
        this.facultyDAO = facultyDAO;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        if (faculty.getName() == null || faculty.getName().trim().isEmpty()) {
            throw new InvalidDataException("name", "Faculty name is required.");
        }
        if (faculty.getEmployeeId() == null || faculty.getEmployeeId().trim().isEmpty()) {
            throw new InvalidDataException("employeeId", "Employee ID is required.");
        }
        Faculty created = facultyDAO.insert(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Faculty> getAllFaculty() {
        return facultyDAO.findAll();
    }

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
        return facultyDAO.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Faculty member with ID " + id + " not found."));
    }

    // Polymorphism: Invoking Reportable interface on Faculty
    @GetMapping("/{id}/report")
    public Map<String, Object> getFacultyReport(@PathVariable Long id) {
        Faculty faculty = getFacultyById(id);
        return Map.of(
                "summary", faculty.getSummary(),
                "reportText", faculty.generateReport(),
                "reportData", faculty.getReportData()
        );
    }
}
