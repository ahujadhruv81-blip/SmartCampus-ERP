# SmartCampus AI-Based ERP — Java Version

This project serves the SmartCampus academic ERP design and runs it from a Java 21 Spring Boot application. Spring Boot serves the rich frontend dashboard at `/`, while exposing a modular Java backend with REST API endpoints, full Object-Oriented design, Collections and Generics, Multithreading & Synchronization, and PostgreSQL / Supabase JDBC database access.

---

## Rubric Mapping (33 Marks Total)

| Rubric Item | Marks | Status | Implementation Details & Source Files |
| :--- | :---: | :---: | :--- |
| **1. OOP Implementation** (Polymorphism, Inheritance, Exception Handling, Interfaces) | 10 | **Present** | • **Inheritance & Abstract Base Class**: [`Person`](file:///src/main/java/com/smartcampus/erp/model/Person.java) with child classes [`Student`](file:///src/main/java/com/smartcampus/erp/model/Student.java) and [`Faculty`](file:///src/main/java/com/smartcampus/erp/model/Faculty.java)<br>• **Interfaces**: [`Reportable`](file:///src/main/java/com/smartcampus/erp/model/Reportable.java) interface implemented by `Student` and `Faculty`<br>• **Polymorphism**: Overridden `getRole()`, `getSummary()`, and `generateReport()` invoked polymorphically in [`StudentService`](file:///src/main/java/com/smartcampus/erp/service/StudentService.java#L85-L95)<br>• **Exception Handling**: Custom exceptions [`StudentNotFoundException`](file:///src/main/java/com/smartcampus/erp/exception/StudentNotFoundException.java), [`InvalidDataException`](file:///src/main/java/com/smartcampus/erp/exception/InvalidDataException.java), [`DatabaseOperationException`](file:///src/main/java/com/smartcampus/erp/exception/DatabaseOperationException.java), and global handling via [`GlobalExceptionHandler`](file:///src/main/java/com/smartcampus/erp/exception/GlobalExceptionHandler.java) |
| **2. Collections & Generics** | 6 | **Present** | • **Domain Collections**: `List<Student>` for rosters, `Set<String>` for unique departments, and `Map<RiskLevel, List<Student>>` for risk grouping in [`StudentService`](file:///src/main/java/com/smartcampus/erp/service/StudentService.java#L50-L75)<br>• **Generic Interface**: [`Repository<T, ID>`](file:///src/main/java/com/smartcampus/erp/dao/Repository.java)<br>• **Generic Utility Methods**: [`CollectionUtils`](file:///src/main/java/com/smartcampus/erp/util/CollectionUtils.java) (`<T> filter()`, `<T, R> mapToSet()`, `<T, K> groupBy()`) |
| **3. Multithreading & Synchronization** | 4 | **Present** | • **ExecutorService**: Dedicated thread pool for background batch report generation in [`ReportGenerationService`](file:///src/main/java/com/smartcampus/erp/service/ReportGenerationService.java#L23-L80)<br>• **Synchronization**: `ReentrantLock` protecting shared report registry and metrics updates, plus a `synchronized` method `recordSystemEvent()` |
| **4. Database Operation Classes (DAO/Repository)** | 7 | **Present** | • [`StudentDAO`](file:///src/main/java/com/smartcampus/erp/dao/StudentDAO.java) with `insert`, `update`, `delete`, `findById`, `findAll`<br>• [`FacultyDAO`](file:///src/main/java/com/smartcampus/erp/dao/FacultyDAO.java) with `insert`, `update`, `delete`, `findById`, `findAll`<br>• [`CourseDAO`](file:///src/main/java/com/smartcampus/erp/dao/CourseDAO.java) with `insert`, `update`, `delete`, `findById`, `findAll` |
| **5. Database Connectivity (JDBC)** | 3 | **Present** | • [`DBConnection`](file:///src/main/java/com/smartcampus/erp/util/DBConnection.java) utility class loading PostgreSQL JDBC connection settings with credentials read from `application.properties` and environment variable overrides |
| **6. Implement JDBC for Database Connectivity** | 3 | **Present** | • Real JDBC operations using `Connection`, `PreparedStatement`, and `ResultSet` with `try-with-resources` inside all DAO classes ([`StudentDAO`](file:///src/main/java/com/smartcampus/erp/dao/StudentDAO.java), [`FacultyDAO`](file:///src/main/java/com/smartcampus/erp/dao/FacultyDAO.java), [`CourseDAO`](file:///src/main/java/com/smartcampus/erp/dao/CourseDAO.java)) targeting PostgreSQL / Supabase |

---

## Requirements
- **JDK 21** (or JDK 17+) installed and available in terminal
- **Maven 3.8+**
- **PostgreSQL / Supabase** account or local PostgreSQL instance (app boots and runs test suite independently)

---

## Setup & How to Run with Supabase (PostgreSQL)

### 1. Database Setup (Supabase)
1. In your [Supabase Dashboard](https://supabase.com/dashboard), open your project.
2. Navigate to **SQL Editor** in the left sidebar.
3. Open or paste the contents of [`docs/schema.sql`](file:///docs/schema.sql) and click **Run**.
   - This creates tables (`students`, `faculty`, `courses`, `departments`, `attendance_records`, `audit_logs`) and seeds sample data.

### 2. Configure Database Credentials (Session Pooler)
Use the Supabase **Session pooler** (port `5432`) connection string:
- In Supabase, go to **Project Settings** → **Database** → **Connection pooling**.
- Select Mode: **Session** (Port `5432`).
- Set your environment variables in PowerShell before running the app (no secrets are stored in code):

```powershell
# Example Supabase Session Pooler configuration:
$env:DB_URL="jdbc:postgresql://aws-0-[region].pooler.supabase.com:5432/postgres?sslmode=require"
$env:DB_USER="postgres.[your-project-ref]"
$env:DB_PASSWORD="your_supabase_db_password"
```

*Note: [`application.properties`](file:///src/main/resources/application.properties) contains placeholder defaults only and will automatically pick up `$env:DB_URL`, `$env:DB_USER`, and `$env:DB_PASSWORD`.*

### 3. Build and Test
Run compilation and the rubric validation test suite:
```powershell
mvn clean compile
mvn test
```

### 4. Run Application
```powershell
mvn spring-boot:run
```
Open [http://localhost:8080](http://localhost:8080) in your browser.

---

## Available API Endpoints

### Student & Faculty ERP Endpoints
- `GET /api/students` — List all students
- `GET /api/students/{id}` — Get student by ID (throws `StudentNotFoundException` if missing)
- `POST /api/students` — Register student (validates data, throws `InvalidDataException`)
- `PUT /api/students/{id}` — Update student
- `DELETE /api/students/{id}` — Delete student
- `GET /api/students/departments` — Unique departments (`Set<String>`)
- `GET /api/students/by-risk` — Students grouped by risk level (`Map<RiskLevel, List<Student>>`)
- `GET /api/students/at-risk` — At-risk students (Generic filter)
- `GET /api/students/{id}/report` — Polymorphic student report
- `GET /api/faculty` — List all faculty
- `GET /api/faculty/{id}/report` — Polymorphic faculty report
- `GET /api/courses` — List all courses

### Multithreading & Synchronization Endpoints
- `POST /api/reports/batch-generate?title=Term+Academic+Audit` — Triggers background `ExecutorService` thread pool to generate aggregate analytics asynchronously
- `GET /api/reports/metrics` — Thread-safe statistics protected by `ReentrantLock`
- `GET /api/reports/{id}` — View generated report details

### Prototype Utility Endpoints
- `GET /api/health` — Application health status
- `GET /api/attendance/calculate?present=19&total=24` — Attendance math
- `GET /api/risk/classify?attendance=68&marks=54&missing=2` — Risk level classifier
