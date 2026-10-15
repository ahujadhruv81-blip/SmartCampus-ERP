-- ==============================================================================
-- SmartCampus AI-Based ERP - Database Schema (PostgreSQL / Supabase)
-- ==============================================================================
-- Run this script in the Supabase SQL Editor (or psql connected to your database).

-- 1. Departments Table
CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(20) NOT NULL UNIQUE,
    head_of_department VARCHAR(100)
);

-- 2. Students Table (Maps to Student entity and StudentDAO)
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    cgpa DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    attendance_percentage DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    risk_level VARCHAR(20) NOT NULL DEFAULT 'LOW',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Faculty Table (Maps to Faculty entity and FacultyDAO)
CREATE TABLE IF NOT EXISTS faculty (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    designation VARCHAR(50) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Courses Table (Maps to Course entity and CourseDAO)
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL DEFAULT 3,
    department VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Attendance Records Table
CREATE TABLE IF NOT EXISTS attendance_records (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    classes_attended INT NOT NULL DEFAULT 0,
    total_classes INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. Audit & Processing Logs Table
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(255) NOT NULL,
    performed_by VARCHAR(100) NOT NULL,
    logged_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================================================
-- Sample Seed Data (PostgreSQL ON CONFLICT DO NOTHING)
-- ==============================================================================

INSERT INTO departments (id, name, code, head_of_department) VALUES
(1, 'Computer Science and Engineering', 'CSE', 'Dr. Ramesh Sharma'),
(2, 'Information Technology', 'IT', 'Dr. Ananya Roy'),
(3, 'Electronics and Communication', 'ECE', 'Dr. Sunita Rao')
ON CONFLICT DO NOTHING;

INSERT INTO students (id, name, email, department, roll_number, cgpa, attendance_percentage, risk_level) VALUES
(1, 'Rahul Verma', 'rahul.verma@smartcampus.edu', 'CSE', '2023CSE001', 8.45, 62.0, 'HIGH'),
(2, 'Priya Singh', 'priya.singh@smartcampus.edu', 'CSE', '2023CSE002', 9.10, 88.5, 'LOW'),
(3, 'Aman Gupta', 'aman.gupta@smartcampus.edu', 'IT', '2023IT014', 6.80, 71.0, 'MEDIUM'),
(4, 'Sneha Patel', 'sneha.patel@smartcampus.edu', 'ECE', '2023ECE008', 7.90, 82.0, 'LOW')
ON CONFLICT DO NOTHING;

INSERT INTO faculty (id, name, email, department, employee_id, designation, specialization) VALUES
(1, 'Dr. Rajesh Nair', 'rajesh.nair@smartcampus.edu', 'CSE', 'FAC-CSE-01', 'Professor', 'Artificial Intelligence & Machine Learning'),
(2, 'Prof. Kavita Rao', 'kavita.rao@smartcampus.edu', 'IT', 'FAC-IT-03', 'Associate Professor', 'Database Systems & Cloud Computing')
ON CONFLICT DO NOTHING;

INSERT INTO courses (id, course_code, course_name, credits, department) VALUES
(1, 'CS301', 'Database Management Systems', 4, 'CSE'),
(2, 'CS302', 'Operating Systems', 4, 'CSE'),
(3, 'CS303', 'Design & Analysis of Algorithms', 4, 'CSE'),
(4, 'IT201', 'Object Oriented Programming with Java', 3, 'IT')
ON CONFLICT DO NOTHING;

-- Reset sequence counters to max id so subsequent inserts auto-increment properly
SELECT setval(pg_get_serial_sequence('departments', 'id'), COALESCE(MAX(id), 1)) FROM departments;
SELECT setval(pg_get_serial_sequence('students', 'id'), COALESCE(MAX(id), 1)) FROM students;
SELECT setval(pg_get_serial_sequence('faculty', 'id'), COALESCE(MAX(id), 1)) FROM faculty;
SELECT setval(pg_get_serial_sequence('courses', 'id'), COALESCE(MAX(id), 1)) FROM courses;
