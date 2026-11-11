# TripSafe — Java Spring Boot REST API Backend

TripSafe is a pre-travel attendance risk projector designed for college hostel students. This backend delivers a high-performance, strictly verified three-tier architecture in Java 17 and Spring Boot 3.

---

## 🏛 Architecture & OOP Design

The backend strictly implements the **Three-Tier Architecture**:
1. **Presentation Layer (Controllers):** REST controllers exposing structured JSON endpoints with Bean Validation (`@Valid`) and unified exception handling (`GlobalExceptionHandler`).
2. **Business Logic Layer (Services & OOP Engines):**
   - `AttendanceProjector`: Encapsulates mathematical projection calculations:
     $$\text{Projected Attendance (\%)} = \frac{\text{Classes Attended}}{\text{Classes Conducted} + \text{Expected Missed Classes}} \times 100$$
   - `RiskAnalyzer`: Implements subject-level threshold checks and the strict collegiate rule: **A trip is marked Safe only if EVERY subject meets its minimum threshold. If even one subject breaches, the trip is AT RISK.**
   - `TripService`: Orchestrates missed class counts from timetable entries, date validations, and the iterative "Find My Maximum Safe Trip" algorithm across a configurable search horizon.
   - `TimetableService`: Resolves day-of-week slots and filters out registered academic holidays/cancellations.
3. **Data Layer (Spring Data JPA & Repositories):**
   - `SubjectRepository`, `TimetableRepository`, `HolidayRepository`, `TripRepository`, `StudentProfileRepository`.

---

## 🚀 Running the Backend

### Prerequisites
- Java Development Kit (JDK 17 or higher)
- Apache Maven 3.8+ (or Maven Wrapper)

### 1. Default Run (Zero-Configuration H2 Embedded Database)
The application starts by default with an embedded, persistent H2 file database. No MySQL installation is required to run out-of-the-box:

```bash
cd backend
mvn spring-boot:run
```

The API will be live at:
- **REST API Base:** `http://localhost:8080/api`
- **Interactive H2 Database Console:** `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:./data/tripsafedb`
  - Username: `sa`
  - Password: *(leave blank)*

### 2. Running with MySQL
To connect to an external MySQL server:
1. Ensure MySQL is running on port 3306 with a user `root` (or update `src/main/resources/application-mysql.properties`).
2. Launch with the `mysql` Spring profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### 3. Running Unit Tests
Execute the complete test suite verifying calculation accuracy and edge cases:
```bash
mvn test
```

---

## 📡 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/dashboard` | Aggregated dashboard statistics, attendance averages, and upcoming trips |
| `GET` | `/api/subjects` | List all tracked subjects with current attendance percentages |
| `POST` | `/api/subjects` | Create a new subject with attendance counts |
| `PUT` | `/api/subjects/{id}` | Update subject details and attendance counts |
| `DELETE`| `/api/subjects/{id}` | Delete a subject (cascades timetable entries) |
| `GET` | `/api/timetable` | Retrieve all weekly timetable slots |
| `POST` | `/api/timetable/slot` | Assign or update a subject in a day/period slot |
| `DELETE`| `/api/timetable/slot/{id}` | Clear a timetable slot |
| `GET` | `/api/holidays` | List registered institutional holidays |
| `POST` | `/api/holidays` | Register a new holiday / cancelled class |
| `DELETE`| `/api/holidays/{id}` | Remove a holiday |
| `POST` | `/api/trips/project` | Calculate projected attendance and risk without persisting |
| `POST` | `/api/trips` | Calculate projected attendance and save the trip to history |
| `POST` | `/api/trips/max-safe-duration`| Find maximum safe travel duration starting from departure date |
| `GET` | `/api/trips` | Retrieve past saved trip history |
| `DELETE`| `/api/trips/{id}` | Delete a trip from history |
| `POST` | `/api/demo/load` | Populate database with realistic college demo dataset |
| `POST` | `/api/demo/reset` | Clear all data for a fresh start |
