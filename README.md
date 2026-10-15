# SmartCampus AI-Based ERP — Java Version

This project keeps the uploaded SmartCampus website design and runs it from a Java Spring Boot application. The supplied HTML file is HTML/CSS/vanilla JavaScript, not a Python source file. Spring Boot serves it at `/`, while Java exposes API endpoints for attendance calculation and student risk classification.

## Requirements
- JDK 17 or later
- Maven 3.8+ installed and available in terminal
- VS Code (recommended extensions: Extension Pack for Java, Spring Boot Extension Pack)

## Run it
1. Extract this ZIP.
2. Open the extracted `smartcampus-java-converted` folder in VS Code.
3. Open Terminal → New Terminal.
4. Run `mvn spring-boot:run`.
5. Open `http://localhost:8080`.

## Test Java API endpoints
- Health: `http://localhost:8080/api/health`
- Attendance calculator: `http://localhost:8080/api/attendance/calculate?present=19&total=24`
- Risk classifier: `http://localhost:8080/api/risk/classify?attendance=68&marks=54&missing=2`

## What is Java and what remains demo-only?
- Spring Boot is the Java server/backend.
- The original single-file HTML/CSS/JavaScript website is kept as the frontend.
- `AttendanceCalculator`, `RiskClassifier`, and `RiskLevel` are reused unchanged from the supplied Java Phase 1 ZIP.
- Existing login screens accept IDs/names without real authentication. Website records are generated in browser JavaScript and are not saved in a database.
- The Java API endpoints work independently; the existing dashboard's mock screens have not yet all been connected to those endpoints.
- No database, trained AI model, real college ERP sync, or payment integration is included.
