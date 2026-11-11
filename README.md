# TripSafe — A Pre-Travel Attendance Risk Projector for Hostel Students

> **“Plan Your Trip. Protect Your Attendance.”**

TripSafe is a full-stack web application developed for college hostel students. It eliminates manual guesswork when planning trips home by mapping weekly academic timetables, bypassing institutional holidays, and projecting subject-wise attendance impacts down to individual class cuts.

---

## 🌟 Key Features

1. **Subject Management:**
   - Add, edit, and delete academic modules.
   - Enforce constraints: attended classes cannot exceed conducted sessions.
   - Handle zero conducted classes without division errors.
2. **Interactive Weekly Timetable:**
   - 5-to-7 day schedule with customizable period slots.
   - Color-coded badges for easy visual recognition.
   - Seamless holiday/cancellation manager so non-instructional days are not counted as cuts.
3. **Core Trip Planner (Pre-Travel Risk Projector):**
   - Inclusive calendar range counting from departure to return date.
   - Calculates exact projected attendance:
     $$\text{Projected Attendance (\%)} = \frac{\text{Classes Attended}}{\text{Classes Conducted} + \text{Expected Missed Classes}} \times 100$$
   - **Strict Multi-Subject College Policy:** If even one subject drops below its minimum threshold (e.g. 75%), the entire trip is flagged **AT RISK**, regardless of a high overall average.
4. **"Find My Maximum Safe Trip":**
   - Automatically evaluates consecutive travel days from a chosen departure date up to a 30-day horizon.
   - Identifies the exact return date and maximum safe days before any subject breaches cutoff.
   - Pinpoints the bottleneck subject constraining the travel window.
5. **Saved Trip History:**
   - Stores travel calculations with serialized snapshots for later review.
6. **Student Settings & Data Controls:**
   - Custom hostel block, roll number, and default target percentages.
   - One-click reload of realistic college demo data.

---

## 🏗 Architecture & OOP Design

TripSafe employs a clean, review-ready **Three-Tier Architecture**:

```
[Presentation Layer]  React 18 + TypeScript + Tailwind CSS (Responsive Dashboard)
         │
         ▼            REST APIs / JSON over HTTP
[Business Logic]      Java 17 + Spring Boot (AttendanceProjector, RiskAnalyzer, TripService)
         │
         ▼            Spring Data JPA / Hibernate
[Data Persistence]    Relational Database (File-based H2 Persistent DB / MySQL)
```

### Java OOP Principles Demonstrated
- **Encapsulation:** Private entity attributes with guarded setters and domain validation methods (`Subject.validateAttendanceValues()`).
- **Abstraction:** Calculation mechanics encapsulated inside pure service components (`AttendanceProjector`, `RiskAnalyzer`).
- **Association:** Relational mapping linking `TimetableEntry` slots to `Subject`, with cascading delete operations.
- **Separation of Concerns:** Isolated HTTP controllers, transactional business services, and database repositories.

---

## ⚡ Quick Start & Demonstration

The workspace includes a dual-mode deployment allowing instant demonstration without requiring pre-installed toolchains, as well as production-ready Maven and npm projects.

### Option 1: Instant Demonstration (Zero External Dependencies)
A complete, self-contained interactive web application is bundled in `web-app/`. It runs on the host's existing Python 3 runtime:

```bash
cd web-app
python3 serve.py
```
Open **[http://localhost:3000](http://localhost:3000)** in Safari or Chrome to immediately test the full dashboard, subject CRUD, timetable editor, trip projector, and maximum safe duration optimizer!

---

### Option 2: Running the Java Spring Boot Backend
Requires JDK 17+ and Apache Maven:

```bash
cd backend

# 1. Run unit tests
mvn test

# 2. Start the backend with default embedded persistent H2 database
mvn spring-boot:run
```
- REST API Base: `http://localhost:8080/api`
- Interactive H2 Database Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/tripsafedb`, User: `sa`, Password: empty)

To run with an external MySQL server:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

### Option 3: Running the React + TypeScript Frontend
Requires Node.js 18+:

```bash
cd frontend
npm install
npm run dev
```
Access the Vite development server at `http://localhost:3000`.

---

## 🧪 Verified Calculation Test Cases

The application's mathematical logic has been verified against the college specification requirements:

| Scenario | Attended / Conducted | Missed Classes | Formula | Projected % | Status |
|---|---|---|---|---|---|
| **Base Attendance** | 60 / 75 | 0 | $60 / 75 \times 100$ | **80.0%** | **SAFE** |
| **5 Missed Classes** | 60 / 75 | +5 | $60 / (75 + 5) \times 100$ | **75.0%** | **SAFE** (Boundary) |
| **6 Missed Classes** | 60 / 75 | +6 | $60 / (75 + 6) \times 100$ | **74.07%** | **AT RISK** (Breach) |

### Strict Multi-Subject Collegiate Rule
If a student has 4 subjects with 90–95% attendance and 1 subject with 74.07% attendance:
- Average attendance = **88.4%** (very high).
- **TripSafe Overall Verdict:** **AT RISK** (Because debarment occurs on a per-subject basis).

---

## 📂 Project Structure

```
tripsafe/
├── backend/                  # Java 17 Spring Boot REST API
│   ├── pom.xml               # Maven dependencies (Spring Web, Data JPA, H2, MySQL, Validation)
│   ├── README.md             # Backend architecture & configuration guide
│   └── src/
│       ├── main/
│       │   ├── java/com/tripsafe/
│       │   │   ├── TripSafeApplication.java
│       │   │   ├── controller/   # Subject, Timetable, Holiday, Trip, Dashboard controllers
│       │   │   ├── dto/          # Request & Response Data Transfer Objects
│       │   │   ├── model/        # Subject, TimetableEntry, Holiday, Trip entities
│       │   │   ├── repository/   # Spring Data JPA repositories
│       │   │   └── service/      # AttendanceProjector, RiskAnalyzer, TripService
│       │   └── resources/
│       │       ├── application.properties         # Default H2 persistent profile
│       │       ├── application-mysql.properties   # MySQL production profile
│       │       ├── schema.sql                     # ANSI SQL table DDL
│       │       └── data.sql                       # Seed demo dataset
│       └── test/java/com/tripsafe/
│           ├── AttendanceProjectorTest.java       # Unit tests for projection formula
│           ├── RiskAnalyzerTest.java              # Unit tests for multi-subject rule
│           └── TripServiceTest.java               # End-to-end trip projection tests
├── frontend/                 # Standard React 18 + TypeScript + Vite project
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   ├── tailwind.config.js
│   └── src/
│       ├── types/            # TypeScript interfaces
│       ├── services/         # api.ts (REST client) & localCalculationEngine.ts
│       └── App.tsx
├── web-app/                  # Instant-run zero-dependency single page application
│   ├── index.html            # Standalone React + Tailwind + Lucide web application
│   └── serve.py              # One-click runner: python3 serve.py
├── docs/                     # College Project Review Documentation
│   ├── ARCHITECTURE.md       # Detailed 3-tier architecture & OOP design notes
│   ├── API_DOCUMENTATION.md  # Comprehensive REST API reference with payloads
│   ├── DATABASE_SCHEMA.md    # ER diagram, table constraints, and 3NF normalization
│   ├── TESTING_REPORT.md     # Detailed test scenario verifications
│   └── ENVIRONMENT_REPORT.md # Environment inspection notes and fallback details
└── README.md
```

---

## 🎓 College Review Presentation Guide

When presenting this project to your college review panel:
1. **Introduction:** State the real hostel problem — students often miscalculate attendance drops before long weekends or festival trips, risking end-term exam debarment.
2. **Live Demo:**
   - Show the **Dashboard** with the 75% threshold marker.
   - Demonstrate **Trip Planner** using the test scenario: enter dates resulting in 5 missed classes (shows 75.0% SAFE), then extend by 1 day to 6 missed classes (shows 74.07% AT RISK).
   - Show **"Find My Maximum Safe Trip"** and explain how the algorithm finds the longest safe departure-return window.
   - Demonstrate how registered **Holidays** prevent false cut penalties.
3. **Architecture & Code Defense:**
   - Highlight the **Three-Tier Architecture** separating Controllers, Business Logic, and Repositories.
   - Show `AttendanceProjector.java` and `RiskAnalyzer.java` demonstrating **Encapsulation** and **Abstraction**.
   - Explain the database normalization and the JSON projection snapshot feature in `Trip.java`.
