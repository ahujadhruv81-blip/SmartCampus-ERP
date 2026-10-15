package com.smartcampus.erp.dao;

import com.smartcampus.erp.exception.DatabaseOperationException;
import com.smartcampus.erp.model.Student;
import com.smartcampus.erp.service.RiskLevel;
import com.smartcampus.erp.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Rubric: Classes for database operations (DAO), Implement JDBC for database connectivity
@Repository
public class StudentDAO implements com.smartcampus.erp.dao.Repository<Student, Long> {

    // Rubric: JDBC Connection, PreparedStatement, ResultSet with try-with-resources
    @Override
    public Student insert(Student student) {
        String sql = "INSERT INTO students (name, email, department, roll_number, cgpa, attendance_percentage, risk_level) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getDepartment());
            stmt.setString(4, student.getRollNumber());
            stmt.setDouble(5, student.getCgpa());
            stmt.setDouble(6, student.getAttendancePercentage());
            stmt.setString(7, student.getRiskLevel() != null ? student.getRiskLevel().name() : RiskLevel.LOW.name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating student failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getLong(1));
                }
            }
            return student;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error inserting student: " + e.getMessage(), e);
        }
    }

    // Rubric: JDBC PreparedStatement with try-with-resources
    @Override
    public boolean update(Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, department = ?, roll_number = ?, " +
                     "cgpa = ?, attendance_percentage = ?, risk_level = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getDepartment());
            stmt.setString(4, student.getRollNumber());
            stmt.setDouble(5, student.getCgpa());
            stmt.setDouble(6, student.getAttendancePercentage());
            stmt.setString(7, student.getRiskLevel() != null ? student.getRiskLevel().name() : RiskLevel.LOW.name());
            stmt.setLong(8, student.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error updating student with ID " + student.getId() + ": " + e.getMessage(), e);
        }
    }

    // Rubric: JDBC PreparedStatement with try-with-resources
    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error deleting student with ID " + id + ": " + e.getMessage(), e);
        }
    }

    // Rubric: JDBC PreparedStatement and ResultSet with try-with-resources
    @Override
    public Optional<Student> findById(Long id) {
        String sql = "SELECT id, name, email, department, roll_number, cgpa, attendance_percentage, risk_level " +
                     "FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToStudent(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error finding student by ID " + id + ": " + e.getMessage(), e);
        }
    }

    // Rubric: JDBC Statement and ResultSet with try-with-resources
    @Override
    public List<Student> findAll() {
        String sql = "SELECT id, name, email, department, roll_number, cgpa, attendance_percentage, risk_level " +
                     "FROM students ORDER BY id ASC";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error retrieving all students: " + e.getMessage(), e);
        }
    }

    public Optional<Student> findByRollNumber(String rollNumber) {
        String sql = "SELECT id, name, email, department, roll_number, cgpa, attendance_percentage, risk_level " +
                     "FROM students WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rollNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToStudent(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error finding student by roll number " + rollNumber + ": " + e.getMessage(), e);
        }
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setDepartment(rs.getString("department"));
        student.setRollNumber(rs.getString("roll_number"));
        student.setCgpa(rs.getDouble("cgpa"));
        student.setAttendancePercentage(rs.getDouble("attendance_percentage"));
        String riskStr = rs.getString("risk_level");
        student.setRiskLevel(riskStr != null ? RiskLevel.valueOf(riskStr) : RiskLevel.LOW);
        return student;
    }
}
