package com.smartcampus.erp.dao;

import com.smartcampus.erp.exception.DatabaseOperationException;
import com.smartcampus.erp.model.Course;
import com.smartcampus.erp.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Rubric: Classes for database operations (DAO), Implement JDBC for database connectivity
@Repository
public class CourseDAO implements com.smartcampus.erp.dao.Repository<Course, Long> {

    @Override
    public Course insert(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, credits, department) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getDepartment());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating course failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getLong(1));
                }
            }
            return course;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error inserting course: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Course course) {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, credits = ?, department = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getDepartment());
            stmt.setLong(5, course.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error updating course with ID " + course.getId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error deleting course with ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Course> findById(Long id) {
        String sql = "SELECT id, course_code, course_name, credits, department FROM courses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCourse(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error finding course by ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> findAll() {
        String sql = "SELECT id, course_code, course_name, credits, department FROM courses ORDER BY id ASC";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                courses.add(mapRowToCourse(rs));
            }
            return courses;
        } catch (SQLException e) {
            throw new DatabaseOperationException("JDBC Error retrieving all courses: " + e.getMessage(), e);
        }
    }

    private Course mapRowToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getLong("id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setCredits(rs.getInt("credits"));
        course.setDepartment(rs.getString("department"));
        return course;
    }
}
