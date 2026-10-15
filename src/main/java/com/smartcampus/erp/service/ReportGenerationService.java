package com.smartcampus.erp.service;

import com.smartcampus.erp.model.Student;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

// Multithreading & Synchronization (ExecutorService, ReentrantLock, synchronized method)
@Service
public class ReportGenerationService {

    // Multithreading: ExecutorService for background asynchronous execution
    private final ExecutorService executorService;

    // Synchronization: ReentrantLock protecting shared data structures
    private final ReentrantLock lock = new ReentrantLock();

    // Shared state across threads
    private final Map<String, Map<String, Object>> completedReports = new ConcurrentHashMap<>();
    private final List<String> executionAuditLogs = new ArrayList<>();
    private int totalReportsGenerated = 0;

    public ReportGenerationService() {
        this.executorService = Executors.newFixedThreadPool(3, new ThreadFactory() {
            private int count = 1;
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "ReportWorkerThread-" + (count++));
                t.setDaemon(true);
                return t;
            }
        });
    }

    // Multithreading: Asynchronous processing with CompletableFuture & ExecutorService
    public CompletableFuture<Map<String, Object>> generateStudentBatchReportAsync(String reportTitle, List<Student> students) {
        return CompletableFuture.supplyAsync(() -> {
            String threadName = Thread.currentThread().getName();
            String reportId = UUID.randomUUID().toString();

            // Simulate realistic report computation and aggregation
            double totalAttendance = 0.0;
            double totalCgpa = 0.0;
            int highRiskCount = 0;

            for (Student s : students) {
                totalAttendance += s.getAttendancePercentage();
                totalCgpa += s.getCgpa();
                if (s.getRiskLevel() == RiskLevel.HIGH) {
                    highRiskCount++;
                }
            }

            int count = students.size();
            double avgAttendance = count > 0 ? (totalAttendance / count) : 0.0;
            double avgCgpa = count > 0 ? (totalCgpa / count) : 0.0;

            Map<String, Object> reportResult = new LinkedHashMap<>();
            reportResult.put("reportId", reportId);
            reportResult.put("title", reportTitle);
            reportResult.put("generatedAt", LocalDateTime.now().toString());
            reportResult.put("processedByThread", threadName);
            reportResult.put("totalStudents", count);
            reportResult.put("averageAttendance", Math.round(avgAttendance * 100.0) / 100.0);
            reportResult.put("averageCgpa", Math.round(avgCgpa * 100.0) / 100.0);
            reportResult.put("highRiskCount", highRiskCount);

            // Synchronization: ReentrantLock protecting shared state updates
            lock.lock();
            try {
                totalReportsGenerated++;
                executionAuditLogs.add(String.format("[%s] Generated report '%s' (ID: %s) by %s",
                        LocalDateTime.now(), reportTitle, reportId, threadName));
                completedReports.put(reportId, reportResult);
            } finally {
                lock.unlock();
            }

            // Synchronization: Synchronized method call
            recordSystemEvent("Completed batch report " + reportId + " on " + threadName);

            return reportResult;
        }, executorService);
    }

    // Synchronization: synchronized method protecting shared log
    public synchronized void recordSystemEvent(String event) {
        String logEntry = String.format("[%s] EVENT: %s", LocalDateTime.now(), event);
        executionAuditLogs.add(logEntry);
    }

    // Thread-safe reading of report metrics using ReentrantLock
    public Map<String, Object> getMetrics() {
        lock.lock();
        try {
            Map<String, Object> metrics = new LinkedHashMap<>();
            metrics.put("totalReportsGenerated", totalReportsGenerated);
            metrics.put("completedReportsCount", completedReports.size());
            metrics.put("recentAuditLogs", new ArrayList<>(executionAuditLogs));
            return metrics;
        } finally {
            lock.unlock();
        }
    }

    public Optional<Map<String, Object>> getReportById(String reportId) {
        return Optional.ofNullable(completedReports.get(reportId));
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
