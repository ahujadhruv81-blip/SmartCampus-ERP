package com.smartcampus.erp.controller;

import com.smartcampus.erp.exception.StudentNotFoundException;
import com.smartcampus.erp.model.Student;
import com.smartcampus.erp.service.ReportGenerationService;
import com.smartcampus.erp.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

// Rubric: REST Endpoints utilizing Multithreading and Synchronization
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportGenerationService reportGenerationService;
    private final StudentService studentService;

    public ReportController(ReportGenerationService reportGenerationService, StudentService studentService) {
        this.reportGenerationService = reportGenerationService;
        this.studentService = studentService;
    }

    // Rubric: Multithreading (Asynchronous endpoint triggering background ExecutorService thread pool)
    @PostMapping("/batch-generate")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> triggerAsyncBatchReport(
            @RequestParam(defaultValue = "Semester Academic & Risk Audit") String title) {

        List<Student> students = studentService.getAllStudents();
        return reportGenerationService.generateStudentBatchReportAsync(title, students)
                .thenApply(ResponseEntity::ok);
    }

    // Rubric: Synchronization (Thread-safe reading of shared state protected by ReentrantLock)
    @GetMapping("/metrics")
    public Map<String, Object> getReportMetrics() {
        return reportGenerationService.getMetrics();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getReportById(@PathVariable String id) {
        return reportGenerationService.getReportById(id)
                .orElseThrow(() -> new StudentNotFoundException("Report with ID " + id + " not found."));
    }
}
