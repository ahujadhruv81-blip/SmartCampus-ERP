package com.smartcampus.erp.controller;

import com.smartcampus.erp.service.AttendanceCalculator;
import com.smartcampus.erp.service.RiskClassifier;
import com.smartcampus.erp.service.RiskLevel;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "UP", "application", "SmartCampus Java ERP");
    }

    @GetMapping("/attendance/calculate")
    public Map<String, Object> calculateAttendance(
            @RequestParam int present,
            @RequestParam int total) {
        if (present < 0 || total < 0 || present > total) {
            throw new IllegalArgumentException("Counts must satisfy 0 <= present <= total.");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("present", present);
        result.put("total", total);
        result.put("percentage", Math.round(AttendanceCalculator.percentage(present, total) * 10.0) / 10.0);
        result.put("requiredPercent", AttendanceCalculator.REQUIRED_PERCENT);
        result.put("classesNeeded", AttendanceCalculator.classesNeededForThreshold(present, total));
        return result;
    }

    @GetMapping("/risk/classify")
    public Map<String, Object> classifyRisk(
            @RequestParam double attendance,
            @RequestParam double marks,
            @RequestParam(defaultValue = "0") int missing) {
        if (attendance < 0 || attendance > 100 || marks < 0 || marks > 100 || missing < 0) {
            throw new IllegalArgumentException("Attendance and marks must be 0–100; missing assignments cannot be negative.");
        }
        RiskLevel level = RiskClassifier.classify(attendance, marks, missing);
        return Map.of("risk", level.name(), "attendance", attendance, "marks", marks,
                "missingAssignments", missing,
                "note", "Rule-based prototype classifier, not a trained machine-learning model.");
    }
}
