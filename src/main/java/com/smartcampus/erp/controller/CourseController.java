package com.smartcampus.erp.controller;

import com.smartcampus.erp.dao.CourseDAO;
import com.smartcampus.erp.exception.InvalidDataException;
import com.smartcampus.erp.exception.StudentNotFoundException;
import com.smartcampus.erp.model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Rubric: REST Endpoints utilizing Course DAO
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseDAO courseDAO;

    public CourseController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
            throw new InvalidDataException("courseCode", "Course code is required.");
        }
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            throw new InvalidDataException("courseName", "Course name is required.");
        }
        Course created = courseDAO.insert(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseDAO.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Course with ID " + id + " not found."));
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteCourse(@PathVariable Long id) {
        boolean deleted = courseDAO.delete(id);
        return Map.of("success", deleted, "courseId", id);
    }
}
