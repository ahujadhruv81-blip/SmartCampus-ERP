# SmartCampus feature mapping (prototype -> Java)

Source analysed: `smartcampus-erp (3).html` (1,153 lines, single file, HTML + CSS + vanilla JS).
Status key: **Done** = written and verified; **Planned** = not yet written. Nothing marked Done unless it was actually run.

| # | Original (page / function) | Existing behaviour | Java target (controller / service / repo / DAO) | Table(s) | Template / resource | Status | Test |
|---|---|---|---|---|---|---|---|
| 1 | Login screen, `loginStudent/Faculty/Admin` | Role cards; any ID/name accepted, no password | `AuthController`, Spring Security `UserDetailsService` | users, roles | `login.html` | Planned | `SecurityRoleTest` |
| 2 | `logout()` | Clears fields, hides app | Spring Security logout (POST + CSRF) | - | `fragments/header` | Planned | `LogoutTest` |
| 3 | `generateStudentData`, `generateFacultyData`, `generateFacultyRoster`, `hashSeed` | Fabricates records from a seeded RNG | **Dropped by design**: replaced by `seed-data.sql` (fictional) | all | - | Planned | `SeedDataTest` |
| 4 | `classesNeededFor75(P,T)` | `3T-4P`; **returns negative when already >= 75%** | `AttendanceCalculator.classesNeededForThreshold` (clamped to 0) | attendance_records | - | **Done** | `AttendanceCalculatorTest` |
| 5 | `riskLevel(att, marks, missing)` | Score rules, High/Medium/Low | `RiskClassifier.classify`, `RiskAssessmentService` | risk_assessments | - | **Done** (classifier) | `RiskClassifierTest` |
| 6 | `renderStudentDash` | CGPA, attendance, assignments, next exam, subject bars, reco, notices. **Hard-coded**: "Good morning, Rahul", date, "62 of 100", "2 pending" | `StudentDashboardController`, `StudentService` | students, enrollments, marks, attendance_records, assignments | `student/dashboard.html` | Planned | `StudentDashboardTest` |
| 7 | `renderStudentSubjects` | Marks, attendance, trend, risk per subject | `StudentService.subjectSummaries` | assessments, marks | `student/subjects.html` | Planned | `SubjectSummaryTest` |
| 8 | `renderStudentAttendance` | Subject bars + recovery plan. **Hard-coded** "AI/ML and OS" | `AttendanceService` | attendance_records | `student/attendance.html` | Planned | `AttendanceServiceTest` |
| 9 | `renderStudentAssign` | Assignment list (static array) | `AssignmentService` | assignments, submissions | `student/assignments.html` | Planned | `AssignmentStatusTest` |
| 10 | `renderStudentTimetable` | 5x5 grid (static) | `TimetableService` | timetable_slots | `student/timetable.html` | Planned | `TimetableTest` |
| 11 | `renderStudentFees` | 3 fee cards (static). Text says "Synced from College ERP" - **no sync exists** | `FeeService` | fee_records | `student/fees.html` | Planned | `FeeServiceTest` |
| 12 | `renderStudentNotices` | Per-student notices | `NotificationService` | notifications | `student/notices.html`, `fragments/notices` | Planned | `NotificationTest` |
| 13 | `renderFacultyDash` | Avg attendance, at-risk count, roster. **Hard-coded** "12 pending", "10:00 AM Room 214", "1 new at-risk" | `FacultyController`, `FacultyService` | faculty, class_assignments | `faculty/dashboard.html` | Planned | `FacultyAuthorizationTest` |
| 14 | `renderFacultyAttendance` + `markSubmitted` | Checkboxes; **button only shows a message, saves nothing** | `FacultyAttendanceController`, `JdbcAttendanceDao` (transactional batch) | attendance_records | `faculty/attendance.html` | Planned | `JdbcAttendanceDaoTest` |
| 15 | `renderFacultyMarks` | Marks inputs; **Publish is fake** | `MarksService` (validate 0-100, publish, notify) | marks | `faculty/marks.html` | Planned | `MarksValidationTest` |
| 16 | `renderFacultyRisk` | Ranked at-risk table, Notify button (**fake**) | `RiskAssessmentService`, `NotificationService` | risk_assessments, notifications | `faculty/risk.html` | Planned | `RiskRankingTest` |
| 17 | `renderFacultyAnnounce` | Audience chips + textarea; **Send is fake**; "(3)" counts hard-coded | `AnnouncementService` | announcements, notifications | `faculty/announce.html` | Planned | `AnnouncementTest` |
| 18 | `renderAdminDash` | Dept stats + bar chart + risk bars. **Constants** (2450 students, 137 at risk); risk bars use odd formulas | `AdminController`, `DepartmentAnalyticsService` (async report) | departments, students | `admin/dashboard.html` | Planned | `DepartmentAnalyticsTest` |
| 19 | `renderAdminDept` | Dept attendance table, Healthy/Watch/Action | same service | attendance_records | `admin/departments.html` | Planned | same |
| 20 | `renderAdminWorkflow` | Static pipeline diagram | `WorkflowStatusService` (real event log) | notifications | `admin/workflow.html` | Planned | `WorkflowTest` |
| 21 | `renderAdminERP` | Static text about REST/JWT. **No real ERP integration** | Documentation page only; stated as not integrated | - | `admin/erp.html` | Planned | `RouteTest` |
| 22 | EduBot (`seedChat`, `askChip`, `sendChat`, `botReply`) | Keyword replies, role-specific chips. **Hard-coded** answers ("AI/ML (52%)", "Maths III 91%") | `ChatController`, `EduBotService` (interface + per-role impls) | uses existing tables | `fragments/chat` + `chat.js` | Planned | `EduBotAuthorizationTest` |
| 23 | Sidebar (`NAV`, `buildSidebar`, `navigate`) | Role-based nav | `fragments/sidebar.html` + `SecurityConfig` URL rules | - | `fragments/sidebar` | Planned | `NavigationRouteTest` |
| 24 | CSS tokens, Fraunces/IBM Plex fonts, masthead, cards, pills, flow diagram | Visual identity | Copy CSS to `static/css/smartcampus.css` | - | static | Planned | manual visual check |

## Defects found in the prototype (documented, not carried over)
1. `classesNeededFor75` returns a negative count when attendance is already above 75%.
2. Dashboard greeting, date, "62 of 100", "2 pending", faculty "12 pending", and several chatbot replies are hard-coded and ignore the logged-in user.
3. Faculty attendance/marks/announcement/notify buttons only reveal a success message; nothing is stored.
4. Login accepts any ID with no password; data is generated from the typed ID.
5. User-typed text is inserted with `innerHTML` (XSS risk); Thymeleaf escapes by default in the port.
6. Admin risk-bar widths use unexplained multipliers (`*6`, `*3`).
7. `pageHead` and several labels claim "Live"/"Synced from College ERP" with no backing system.

## Limitations of this analysis
Only the one HTML file was provided; no other source files, images, or data. No exam schedule data exists beyond a randomly generated "next exam", so an `exams` table is added as a new feature.
