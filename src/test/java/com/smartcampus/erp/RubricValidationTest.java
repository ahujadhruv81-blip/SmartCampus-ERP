package com.smartcampus.erp;

import com.smartcampus.erp.exception.InvalidDataException;
import com.smartcampus.erp.exception.StudentNotFoundException;
import com.smartcampus.erp.model.Faculty;
import com.smartcampus.erp.model.Person;
import com.smartcampus.erp.model.Reportable;
import com.smartcampus.erp.model.Student;
import com.smartcampus.erp.service.ReportGenerationService;
import com.smartcampus.erp.service.RiskLevel;
import com.smartcampus.erp.util.CollectionUtils;
import com.smartcampus.erp.util.DBConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class RubricValidationTest {

    // =========================================================================
    // Rubric 1: OOP Implementation (Polymorphism, Inheritance, Interfaces, Exceptions)
    // =========================================================================

    @Test
    @DisplayName("OOP: Inheritance - Student and Faculty must extend Person")
    void testInheritance() {
        Student student = new Student(1L, "Aarav Sharma", "aarav@campus.edu", "CSE", "2023CSE01", 8.8, 85.0, RiskLevel.LOW);
        Faculty faculty = new Faculty(2L, "Dr. Gupta", "gupta@campus.edu", "CSE", "FAC01", "Professor", "AI");

        assertTrue(student instanceof Person, "Student must inherit from Person");
        assertTrue(faculty instanceof Person, "Faculty must inherit from Person");
    }

    @Test
    @DisplayName("OOP: Interfaces - Student and Faculty must implement Reportable")
    void testInterfaces() {
        Student student = new Student(1L, "Aarav Sharma", "aarav@campus.edu", "CSE", "2023CSE01", 8.8, 85.0, RiskLevel.LOW);
        Faculty faculty = new Faculty(2L, "Dr. Gupta", "gupta@campus.edu", "CSE", "FAC01", "Professor", "AI");

        assertTrue(student instanceof Reportable, "Student must implement Reportable interface");
        assertTrue(faculty instanceof Reportable, "Faculty must implement Reportable interface");
        assertNotNull(student.generateReport());
        assertNotNull(faculty.generateReport());
    }

    @Test
    @DisplayName("OOP: Polymorphism - Overridden getRole() and getSummary() invoked via base Person reference")
    void testPolymorphism() {
        Person p1 = new Student(1L, "Aarav", "aarav@campus.edu", "CSE", "202301", 8.5, 80.0, RiskLevel.LOW);
        Person p2 = new Faculty(2L, "Dr. Raman", "raman@campus.edu", "ECE", "FAC02", "Dean", "VLSI");

        assertEquals("STUDENT", p1.getRole(), "Polymorphic call must return STUDENT");
        assertEquals("FACULTY", p2.getRole(), "Polymorphic call must return FACULTY");
        assertTrue(p1.getSummary().contains("Student:"));
        assertTrue(p2.getSummary().contains("Faculty:"));
    }

    @Test
    @DisplayName("OOP: Exception Handling - Custom exceptions thrown and caught")
    void testCustomExceptions() {
        StudentNotFoundException notFound = assertThrows(StudentNotFoundException.class, () -> {
            throw new StudentNotFoundException(999L);
        });
        assertTrue(notFound.getMessage().contains("999"));

        InvalidDataException invalidData = assertThrows(InvalidDataException.class, () -> {
            throw new InvalidDataException("cgpa", "CGPA must be between 0.0 and 10.0.");
        });
        assertTrue(invalidData.getMessage().contains("cgpa"));
    }

    // =========================================================================
    // Rubric 2: Collections & Generics (List, Set, Map, Generic methods)
    // =========================================================================

    @Test
    @DisplayName("Collections & Generics: List, Set, Map and generic methods in CollectionUtils")
    void testCollectionsAndGenerics() {
        List<Student> students = List.of(
                new Student(1L, "Alice", "alice@campus.edu", "CSE", "R1", 9.0, 90.0, RiskLevel.LOW),
                new Student(2L, "Bob", "bob@campus.edu", "IT", "R2", 6.5, 60.0, RiskLevel.HIGH),
                new Student(3L, "Charlie", "charlie@campus.edu", "CSE", "R3", 7.2, 70.0, RiskLevel.MEDIUM)
        );

        // Generic filter
        List<Student> highRisk = CollectionUtils.filter(students, s -> s.getRiskLevel() == RiskLevel.HIGH);
        assertEquals(1, highRisk.size());
        assertEquals("Bob", highRisk.get(0).getName());

        // Generic map to Set (unique departments)
        Set<String> departments = CollectionUtils.mapToSet(students, s -> s.getDepartment());
        assertEquals(2, departments.size());
        assertTrue(departments.contains("CSE"));
        assertTrue(departments.contains("IT"));

        // Generic groupBy to Map
        Map<RiskLevel, List<Student>> grouped = CollectionUtils.groupBy(students, s -> s.getRiskLevel());
        assertEquals(3, grouped.size());
        assertEquals(1, grouped.get(RiskLevel.LOW).size());
    }

    // =========================================================================
    // Rubric 3: Multithreading & Synchronization (ExecutorService, ReentrantLock)
    // =========================================================================

    @Test
    @DisplayName("Multithreading & Synchronization: ExecutorService with ReentrantLock-protected shared metrics")
    void testMultithreadingAndSynchronization() throws Exception {
        ReportGenerationService service = new ReportGenerationService();
        List<Student> students = List.of(
                new Student(1L, "Alice", "alice@campus.edu", "CSE", "R1", 9.0, 90.0, RiskLevel.LOW),
                new Student(2L, "Bob", "bob@campus.edu", "IT", "R2", 6.0, 55.0, RiskLevel.HIGH)
        );

        CompletableFuture<Map<String, Object>> futureReport =
                service.generateStudentBatchReportAsync("Annual Review", students);

        Map<String, Object> report = futureReport.get();
        assertNotNull(report);
        assertEquals(2, report.get("totalStudents"));
        assertTrue(((String) report.get("processedByThread")).startsWith("ReportWorkerThread-"));

        // Check synchronized / locked metrics
        Map<String, Object> metrics = service.getMetrics();
        assertTrue((int) metrics.get("totalReportsGenerated") >= 1);

        service.shutdown();
    }

    // =========================================================================
    // Rubric 5 & 6: Database Connectivity (JDBC)
    // =========================================================================

    @Test
    @DisplayName("JDBC: DBConnection class loads configuration from application.properties")
    void testDBConnectionConfiguration() {
        assertNotNull(DBConnection.getUrl());
        assertNotNull(DBConnection.getUsername());
        assertTrue(DBConnection.getUrl().contains("postgresql"));
    }
}
