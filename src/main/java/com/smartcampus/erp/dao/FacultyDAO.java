package com.smartcampus.erp.dao;

import com.smartcampus.erp.exception.DatabaseOperationException;
import com.smartcampus.erp.model.Faculty;
import com.smartcampus.erp.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Database Operations: Faculty DAO implementing generic Repository with JDBC
@Repository
public class FacultyDAO implements com.smartcampus.erp.dao.Repository<Faculty, Long> {

    @Override
    public Faculty insert(Faculty faculty) {
        String sql = "INSERT INTO faculty (name, email, department, employee_id, designation, specialization) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, faculty.getName());
            stmt.setString(2, faculty.getEmail());
            stmt.setString(3, faculty.getDepartment());
            stmt.setString(4, faculty.getEmployeeId());
            stmt.setString(5, faculty.getDesignation());
            stmt.setString(6, faculty.getSpecialization());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating faculty failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    faculty.setId(generatedKeys.getLong(1));
                }
            }
            return faculty;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error inserting faculty: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Faculty faculty) {
        String sql = "UPDATE faculty SET name = ?, email = ?, department = ?, employee_id = ?, " +
                     "designation = ?, specialization = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, faculty.getName());
            stmt.setString(2, faculty.getEmail());
            stmt.setString(3, faculty.getDepartment());
            stmt.setString(4, faculty.getEmployeeId());
            stmt.setString(5, faculty.getDesignation());
            stmt.setString(6, faculty.getSpecialization());
            stmt.setLong(7, faculty.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error updating faculty with ID " + faculty.getId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM faculty WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error deleting faculty with ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Faculty> findById(Long id) {
        String sql = "SELECT id, name, email, department, employee_id, designation, specialization " +
                     "FROM faculty WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToFaculty(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error finding faculty by ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Faculty> findAll() {
        String sql = "SELECT id, name, email, department, employee_id, designation, specialization " +
                     "FROM faculty ORDER BY id ASC";
        List<Faculty> facultyList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                facultyList.add(mapRowToFaculty(rs));
            }
            return facultyList;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error retrieving all faculty: " + e.getMessage(), e);
        }
    }

    private Faculty mapRowToFaculty(ResultSet rs) throws SQLException {
        Faculty faculty = new Faculty();
        faculty.setId(rs.getLong("id"));
        faculty.setName(rs.getString("name"));
        faculty.setEmail(rs.getString("email"));
        faculty.setDepartment(rs.getString("department"));
        faculty.setEmployeeId(rs.getString("employee_id"));
        faculty.setDesignation(rs.getString("designation"));
        faculty.setSpecialization(rs.getString("specialization"));
        return faculty;
    }
}
