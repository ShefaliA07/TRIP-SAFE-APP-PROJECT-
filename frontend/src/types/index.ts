export type RiskStatus = 'SAFE' | 'AT_RISK';

export type DayOfWeekEnum = 
  | 'MONDAY' 
  | 'TUESDAY' 
  | 'WEDNESDAY' 
  | 'THURSDAY' 
  | 'FRIDAY' 
  | 'SATURDAY' 
  | 'SUNDAY';

export interface Subject {
  id: number;
  name: string;
  code: string;
  classesConducted: number;
  classesAttended: number;
  minAttendancePercentage: number;
  color: string;
  currentAttendancePercentage: number;
  currentStatus: RiskStatus;
}

export interface TimetableEntry {
  id?: number;
  dayOfWeek: DayOfWeekEnum;
  periodIndex: number;
  slotTime: string;
  subjectId: number;
  subjectName?: string;
  subjectCode?: string;
  color?: string;
}

export interface Holiday {
  id?: number;
  date: string; // YYYY-MM-DD
  name: string;
  description?: string;
}

export interface SubjectProjectionResult {
  subjectId: number;
  subjectName: string;
  subjectCode: string;
  color: string;
  classesConductedBefore: number;
  classesAttended: number;
  currentAttendancePercentage: number;
  expectedMissedClasses: number;
  classesConductedAfter: number;
  projectedAttendancePercentage: number;
  requiredAttendancePercentage: number;
  status: RiskStatus;
  riskMargin: number;
  explanation: string;
}

export interface TripProjectionResponse {
  tripName: string;
  departureDate: string;
  returnDate: string;
  durationDays: number;
  overallStatus: RiskStatus;
  safeSubjectsCount: number;
  atRiskSubjectsCount: number;
  totalMissedClasses: number;
  subjectProjections: SubjectProjectionResult[];
  bottleneckSubjectName?: string;
  mostAffectedSubjects: string[];
  summaryMessage: string;
  recommendationMessage: string;
  savedTripId?: number;
}

export interface MaxSafeDurationResponse {
  departureDate: string;
  recommendedReturnDate: string;
  maxSafeDays: number;
  departureDateSafe: boolean;
  reachedHorizonWithoutFailure: boolean;
  searchHorizonDays: number;
  limitingSubjectName?: string;
  limitingSubjectCode?: string;
  limitingSubjectMargin?: number;
  explanation: string;
  noClassesMissed: boolean;
  projectedResultsAtRecommendedDate: SubjectProjectionResult[];
}

export interface Trip {
  id: number;
  tripName: string;
  departureDate: string;
  returnDate: string;
  notes?: string;
  overallStatus: RiskStatus;
  safeSubjectsCount: number;
  atRiskSubjectsCount: number;
  totalClassesMissed: number;
  projectionDetailsJson?: string;
  createdAt: string;
}

export interface StudentProfile {
  name: string;
  rollNumber: string;
  collegeName: string;
  hostelBlock: string;
  defaultMinAttendance: number;
}

export interface DashboardSummary {
  totalSubjects: number;
  averageAttendance: number;
  safeSubjectsCount: number;
  atRiskSubjectsCount: number;
  upcomingTrip?: Trip;
  subjects: Subject[];
  recentTrips: Trip[];
}
