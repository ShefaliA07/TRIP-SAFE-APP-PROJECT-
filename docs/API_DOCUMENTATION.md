# TripSafe — REST API Documentation

Base URL: `http://localhost:8080/api`

---

## 1. Dashboard API

### `GET /api/dashboard`
Returns high-level statistics, average attendance, and upcoming trips.

**Response `200 OK`:**
```json
{
  "totalSubjects": 5,
  "averageAttendance": 81.49,
  "safeSubjectsCount": 5,
  "atRiskSubjectsCount": 0,
  "upcomingTrip": {
    "id": 1,
    "tripName": "Home Weekend Visit",
    "departureDate": "2026-11-02",
    "returnDate": "2026-11-06",
    "overallStatus": "SAFE",
    "totalClassesMissed": 4
  },
  "subjects": [ ... ],
  "recentTrips": [ ... ]
}
```

---

## 2. Subjects API

### `GET /api/subjects`
List all enrolled subjects with current attendance.

### `POST /api/subjects`
Create a new subject.

**Request Payload:**
```json
{
  "name": "Operating Systems",
  "code": "CS303",
  "classesConducted": 65,
  "classesAttended": 52,
  "minAttendancePercentage": 75.0,
  "color": "#059669"
}
```

### `PUT /api/subjects/{id}`
Update an existing subject.

### `DELETE /api/subjects/{id}`
Deletes a subject and cascades removal of associated weekly timetable slots.

---

## 3. Weekly Timetable & Holidays API

### `GET /api/timetable`
Retrieves all configured weekly timetable slots.

### `POST /api/timetable/slot`
Assigns or updates a timetable slot.

**Request Payload:**
```json
{
  "dayOfWeek": "MONDAY",
  "periodIndex": 1,
  "slotTime": "09:00 - 10:00",
  "subjectId": 1
}
```

### `DELETE /api/timetable/slot/{day}/{periodIndex}`
Clears an assigned timetable slot.

### `GET /api/holidays`
List all registered institutional holidays / cancelled classes.

### `POST /api/holidays`
Register a holiday to be deducted from travel missed class counts.

**Request Payload:**
```json
{
  "date": "2026-11-05",
  "name": "College Foundation Day",
  "description": "Annual sports and cultural festival"
}
```

---

## 4. Trip Planning & Attendance Projection API

### `POST /api/trips/project`
Simulate a trip projection without saving to history.

**Request Payload:**
```json
{
  "tripName": "Diwali Festival Trip",
  "departureDate": "2026-11-02",
  "returnDate": "2026-11-06",
  "notes": "Travelling to hometown by train",
  "saveTrip": false
}
```

**Response `200 OK`:**
```json
{
  "tripName": "Diwali Festival Trip",
  "departureDate": "2026-11-02",
  "returnDate": "2026-11-06",
  "durationDays": 5,
  "overallStatus": "SAFE",
  "safeSubjectsCount": 5,
  "atRiskSubjectsCount": 0,
  "totalMissedClasses": 5,
  "subjectProjections": [
    {
      "subjectId": 1,
      "subjectName": "Data Structures & Algorithms",
      "subjectCode": "CS301",
      "classesConductedBefore": 75,
      "classesAttended": 60,
      "currentAttendancePercentage": 80.0,
      "expectedMissedClasses": 5,
      "classesConductedAfter": 80,
      "projectedAttendancePercentage": 75.0,
      "requiredAttendancePercentage": 75.0,
      "status": "SAFE",
      "riskMargin": 0.0,
      "explanation": "Missing 5 classes drops attendance from 80.0% to 75.0% (safe with a 0.0% cushion above the 75% requirement)."
    }
  ],
  "bottleneckSubjectName": "Data Structures & Algorithms",
  "summaryMessage": "Trip is Safe! All 5 subjects maintain required attendance.",
  "recommendationMessage": "You can travel without risking minimum attendance requirements. Have a safe journey!"
}
```

### `POST /api/trips`
Simulate and persist a planned trip to history (`saveTrip = true`).

### `POST /api/trips/max-safe-duration`
Find maximum consecutive safe travel days starting from a given departure date.

**Request Payload:**
```json
{
  "departureDate": "2026-11-02",
  "searchHorizonDays": 30
}
```

**Response `200 OK`:**
```json
{
  "departureDate": "2026-11-02",
  "recommendedReturnDate": "2026-11-06",
  "maxSafeDays": 5,
  "departureDateSafe": true,
  "reachedHorizonWithoutFailure": false,
  "searchHorizonDays": 30,
  "limitingSubjectName": "Data Structures & Algorithms",
  "explanation": "You can safely travel for 5 day(s) until 2026-11-06. Extending to 2026-11-07 triggers risk in 'Data Structures & Algorithms'.",
  "projectedResultsAtRecommendedDate": [ ... ]
}
```

---

## 5. Demo & Administration API

### `POST /api/demo/load`
Loads sample college subjects, timetable, and holiday data.

### `POST /api/demo/reset`
Clears all application data.
