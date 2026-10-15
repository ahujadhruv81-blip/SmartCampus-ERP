package com.smartcampus.erp.service;

import com.smartcampus.erp.dao.StudentDAO;
import com.smartcampus.erp.exception.InvalidDataException;
import com.smartcampus.erp.exception.StudentNotFoundException;
import com.smartcampus.erp.model.Person;
import com.smartcampus.erp.model.Reportable;
import com.smartcampus.erp.model.Student;
import com.smartcampus.erp.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

// Service Layer: Business Logic, Collections (List, Set, Map), and Exception Handling
@Service
public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    // Exception Handling: Validation with InvalidDataException
    public Student registerStudent(Student student) {
        if (student == null) {
            throw new InvalidDataException("student", "Student payload cannot be null.");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new InvalidDataException("name", "Student name is required.");
        }
        if (student.getRollNumber() == null || student.getRollNumber().trim().isEmpty()) {
            throw new InvalidDataException("rollNumber", "Roll number is required.");
        }
        if (student.getCgpa() < 0.0 || student.getCgpa() > 10.0) {
            throw new InvalidDataException("cgpa", "CGPA must be between 0.0 and 10.0.");
        }
        if (student.getAttendancePercentage() < 0.0 || student.getAttendancePercentage() > 100.0) {
            throw new InvalidDataException("attendancePercentage", "Attendance percentage must be between 0.0 and 100.0.");
        }

        // Compute initial risk level dynamically using RiskClassifier
        if (student.getRiskLevel() == null) {
            student.setRiskLevel(RiskClassifier.classify(student.getAttendancePercentage(), student.getCgpa() * 10.0, 0));
        }

        return studentDAO.insert(student);
    }

    // Exception Handling: Custom StudentNotFoundException
    public Student getStudentById(Long id) {
        return studentDAO.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    // Collections & Generics: List<Student>
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    // Collections & Generics: Set<String> for unique departments
    public Set<String> getUniqueDepartments() {
        List<Student> students = studentDAO.findAll();
        return CollectionUtils.mapToSet(students, s -> s.getDepartment());
    }

    // Collections & Generics: Map<RiskLevel, List<Student>> grouping
    public Map<RiskLevel, List<Student>> getStudentsGroupedByRisk() {
        List<Student> students = studentDAO.findAll();
        return CollectionUtils.groupBy(students, s -> s.getRiskLevel());
    }

    // Collections & Generics: Generic filter method
    public List<Student> getAtRiskStudents() {
        List<Student> students = studentDAO.findAll();
        return CollectionUtils.filter(students, s -> s.getRiskLevel() == RiskLevel.HIGH || s.getRiskLevel() == RiskLevel.MEDIUM);
    }

    public boolean updateStudent(Long id, Student student) {
        if (!studentDAO.findById(id).isPresent()) {
            throw new StudentNotFoundException(id);
        }
        student.setId(id);
        return studentDAO.update(student);
    }

    public boolean deleteStudent(Long id) {
        if (!studentDAO.findById(id).isPresent()) {
            throw new StudentNotFoundException(id);
        }
        return studentDAO.delete(id);
    }

    // Polymorphism: Invoking overridden methods on Person & Reportable interfaces
    public String generatePersonSummary(Person person) {
        return person.getSummary(); // Polymorphic invocation
    }

    public String generateReport(Reportable reportable) {
        return reportable.generateReport(); // Polymorphic invocation
    }
}
