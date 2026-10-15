package com.smartcampus.erp.service;

/**
 * Deterministic, rule-based risk classification (NOT a trained AI model).
 * Rules are carried over unchanged from riskLevel() in the HTML prototype.
 * The score is kept in tenths so no floating-point comparison is needed.
 *   attendance < 75 : +2.0    attendance < 60 : +1.0 more
 *   marks      < 60 : +2.0    marks      < 45 : +1.0 more
 *   each missing assignment (at most 3 counted): +0.6
 *   score >= 4.0 HIGH, >= 2.0 MEDIUM, otherwise LOW
 */
public final class RiskClassifier {

    private RiskClassifier() { }

    public static RiskLevel classify(double attendancePct, double avgMarksPct, int missingAssignments) {
        int tenths = 0;
        if (attendancePct < 75) tenths += 20;
        if (attendancePct < 60) tenths += 10;
        if (avgMarksPct < 60) tenths += 20;
        if (avgMarksPct < 45) tenths += 10;
        tenths += Math.min(Math.max(missingAssignments, 0), 3) * 6;
        if (tenths >= 40) return RiskLevel.HIGH;
        if (tenths >= 20) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }
}
