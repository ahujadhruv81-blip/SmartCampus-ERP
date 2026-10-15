package com.smartcampus.erp.model;

import java.util.Map;

// OOP: Interfaces
public interface Reportable {
    String generateReport();
    Map<String, Object> getReportData();
}
