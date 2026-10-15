package com.smartcampus.erp.service;

/**
 * Pure attendance maths (no Spring, no DB) so it is trivially unit-testable.
 * The threshold is kept in percent so the rule uses integers only.
 */
public final class AttendanceCalculator {

    public static final int REQUIRED_PERCENT = 75;

    private AttendanceCalculator() { }

    /** attendancePercentage = present * 100.0 / total; 0 when no classes have been held. */
    public static double percentage(int present, int total) {
        validate(present, total);
        return total == 0 ? 0.0 : (present * 100.0) / total;
    }

    /**
     * Smallest x >= 0 such that (P + x) / (T + x) >= 75/100.
     * Algebra: 100(P+x) >= 75(T+x)  =>  25x >= 75T - 100P  =>  x >= 3T - 4P.
     * Already >= 75% (or no classes yet) returns 0. Because P <= T the target is always reachable.
     */
    public static int classesNeededForThreshold(int present, int total) {
        validate(present, total);
        long needed = 3L * total - 4L * present;
        return (int) Math.max(0, needed);
    }

    private static void validate(int present, int total) {
        if (present < 0 || total < 0 || present > total) {
            throw new IllegalArgumentException(
                "Invalid attendance counts: present=" + present + ", total=" + total);
        }
    }
}
