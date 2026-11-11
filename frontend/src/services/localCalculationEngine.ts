import {
  Subject,
  TimetableEntry,
  Holiday,
  Trip,
  DayOfWeekEnum,
  SubjectProjectionResult,
  TripProjectionResponse,
  MaxSafeDurationResponse,
  DashboardSummary,
  RiskStatus
} from '../types';

const INITIAL_SUBJECTS: Subject[] = [
  { id: 1, name: 'Data Structures & Algorithms', code: 'CS301', classesConducted: 75, classesAttended: 60, minAttendancePercentage: 75, color: '#4f46e5', currentAttendancePercentage: 80.0, currentStatus: 'SAFE' },
  { id: 2, name: 'Database Management Systems', code: 'CS302', classesConducted: 55, classesAttended: 48, minAttendancePercentage: 75, color: '#0891b2', currentAttendancePercentage: 87.27, currentStatus: 'SAFE' },
  { id: 3, name: 'Operating Systems', code: 'CS303', classesConducted: 65, classesAttended: 52, minAttendancePercentage: 75, color: '#059669', currentAttendancePercentage: 80.0, currentStatus: 'SAFE' },
  { id: 4, name: 'Computer Networks', code: 'CS304', classesConducted: 50, classesAttended: 39, minAttendancePercentage: 75, color: '#d97706', currentAttendancePercentage: 78.0, currentStatus: 'SAFE' },
  { id: 5, name: 'Software Engineering', code: 'CS305', classesConducted: 45, classesAttended: 37, minAttendancePercentage: 75, color: '#7c3aed', currentAttendancePercentage: 82.22, currentStatus: 'SAFE' },
];

const INITIAL_TIMETABLE: TimetableEntry[] = [
  { id: 1, dayOfWeek: 'MONDAY', periodIndex: 1, slotTime: '09:00 - 10:00', subjectId: 1 },
  { id: 2, dayOfWeek: 'MONDAY', periodIndex: 2, slotTime: '10:15 - 11:15', subjectId: 2 },
  { id: 3, dayOfWeek: 'MONDAY', periodIndex: 3, slotTime: '11:30 - 12:30', subjectId: 3 },
  { id: 4, dayOfWeek: 'MONDAY', periodIndex: 4, slotTime: '02:00 - 03:00', subjectId: 4 },

  { id: 5, dayOfWeek: 'TUESDAY', periodIndex: 1, slotTime: '09:00 - 10:00', subjectId: 5 },
  { id: 6, dayOfWeek: 'TUESDAY', periodIndex: 2, slotTime: '10:15 - 11:15', subjectId: 1 },
  { id: 7, dayOfWeek: 'TUESDAY', periodIndex: 3, slotTime: '11:30 - 12:30', subjectId: 2 },
  { id: 8, dayOfWeek: 'TUESDAY', periodIndex: 4, slotTime: '02:00 - 03:00', subjectId: 3 },

  { id: 9, dayOfWeek: 'WEDNESDAY', periodIndex: 1, slotTime: '09:00 - 10:00', subjectId: 4 },
  { id: 10, dayOfWeek: 'WEDNESDAY', periodIndex: 2, slotTime: '10:15 - 11:15', subjectId: 5 },
  { id: 11, dayOfWeek: 'WEDNESDAY', periodIndex: 3, slotTime: '11:30 - 12:30', subjectId: 1 },
  { id: 12, dayOfWeek: 'WEDNESDAY', periodIndex: 4, slotTime: '02:00 - 03:00', subjectId: 2 },

  { id: 13, dayOfWeek: 'THURSDAY', periodIndex: 1, slotTime: '09:00 - 10:00', subjectId: 3 },
  { id: 14, dayOfWeek: 'THURSDAY', periodIndex: 2, slotTime: '10:15 - 11:15', subjectId: 4 },
  { id: 15, dayOfWeek: 'THURSDAY', periodIndex: 3, slotTime: '11:30 - 12:30', subjectId: 5 },
  { id: 16, dayOfWeek: 'THURSDAY', periodIndex: 4, slotTime: '02:00 - 03:00', subjectId: 1 },

  { id: 17, dayOfWeek: 'FRIDAY', periodIndex: 1, slotTime: '09:00 - 10:00', subjectId: 2 },
  { id: 18, dayOfWeek: 'FRIDAY', periodIndex: 2, slotTime: '10:15 - 11:15', subjectId: 3 },
  { id: 19, dayOfWeek: 'FRIDAY', periodIndex: 3, slotTime: '11:30 - 12:30', subjectId: 4 },
  { id: 20, dayOfWeek: 'FRIDAY', periodIndex: 4, slotTime: '02:00 - 03:00', subjectId: 5 },
];

const INITIAL_HOLIDAYS: Holiday[] = [
  { id: 1, date: '2026-11-05', name: 'College Foundation Day', description: 'Annual cultural day - no instructional classes' }
];

const INITIAL_TRIPS: Trip[] = [
  {
    id: 1,
    tripName: 'Diwali Homecoming',
    departureDate: '2026-10-15',
    returnDate: '2026-10-19',
    notes: 'Travelled home for festival break',
    overallStatus: 'SAFE',
    safeSubjectsCount: 5,
    atRiskSubjectsCount: 0,
    totalClassesMissed: 4,
    createdAt: new Date().toISOString()
  }
];

class LocalCalculationEngine {
  private subjects: Subject[] = [];
  private timetable: TimetableEntry[] = [];
  private holidays: Holiday[] = [];
  private trips: Trip[] = [];

  constructor() {
    this.loadState();
  }

  private loadState() {
    try {
      const storedSub = localStorage.getItem('tripsafe_subjects');
      const storedTt = localStorage.getItem('tripsafe_timetable');
      const storedHol = localStorage.getItem('tripsafe_holidays');
      const storedTrips = localStorage.getItem('tripsafe_trips');

      this.subjects = storedSub ? JSON.parse(storedSub) : INITIAL_SUBJECTS;
      this.timetable = storedTt ? JSON.parse(storedTt) : INITIAL_TIMETABLE;
      this.holidays = storedHol ? JSON.parse(storedHol) : INITIAL_HOLIDAYS;
      this.trips = storedTrips ? JSON.parse(storedTrips) : INITIAL_TRIPS;
    } catch {
      this.subjects = INITIAL_SUBJECTS;
      this.timetable = INITIAL_TIMETABLE;
      this.holidays = INITIAL_HOLIDAYS;
      this.trips = INITIAL_TRIPS;
    }
  }

  private saveState() {
    try {
      localStorage.setItem('tripsafe_subjects', JSON.stringify(this.subjects));
      localStorage.setItem('tripsafe_timetable', JSON.stringify(this.timetable));
      localStorage.setItem('tripsafe_holidays', JSON.stringify(this.holidays));
      localStorage.setItem('tripsafe_trips', JSON.stringify(this.trips));
    } catch (e) {
      console.warn('Could not save to localStorage:', e);
    }
  }

  public resetDemoData() {
    this.subjects = INITIAL_SUBJECTS;
    this.timetable = INITIAL_TIMETABLE;
    this.holidays = INITIAL_HOLIDAYS;
    this.trips = INITIAL_TRIPS;
    this.saveState();
  }

  public clearAllData() {
    this.subjects = [];
    this.timetable = [];
    this.holidays = [];
    this.trips = [];
    this.saveState();
  }

  // Attendance Math
  public calculateCurrentPercentage(attended: number, conducted: number): number {
    if (conducted <= 0) return 100.0;
    return (attended / conducted) * 100.0;
  }

  public calculateProjectedPercentage(attended: number, conducted: number, missed: number): number {
    const total = conducted + missed;
    if (total <= 0) return 100.0;
    return (attended / total) * 100.0;
  }

  public getSubjects(): Subject[] {
    return this.subjects.map(s => {
      const pct = this.calculateCurrentPercentage(s.classesAttended, s.classesConducted);
      return {
        ...s,
        currentAttendancePercentage: pct,
        currentStatus: pct >= s.minAttendancePercentage ? 'SAFE' : 'AT_RISK'
      };
    });
  }

  public addSubject(sub: Omit<Subject, 'id' | 'currentAttendancePercentage' | 'currentStatus'>): Subject {
    if (sub.classesAttended > sub.classesConducted) {
      throw new Error('Classes attended cannot exceed classes conducted.');
    }
    const newId = this.subjects.length > 0 ? Math.max(...this.subjects.map(s => s.id)) + 1 : 1;
    const pct = this.calculateCurrentPercentage(sub.classesAttended, sub.classesConducted);
    const created: Subject = {
      ...sub,
      id: newId,
      currentAttendancePercentage: pct,
      currentStatus: pct >= sub.minAttendancePercentage ? 'SAFE' : 'AT_RISK'
    };
    this.subjects.push(created);
    this.saveState();
    return created;
  }

  public updateSubject(id: number, sub: Partial<Subject>): Subject {
    const index = this.subjects.findIndex(s => s.id === id);
    if (index === -1) throw new Error('Subject not found');
    const existing = this.subjects[index];
    const attended = sub.classesAttended ?? existing.classesAttended;
    const conducted = sub.classesConducted ?? existing.classesConducted;
    if (attended > conducted) {
      throw new Error('Classes attended cannot exceed classes conducted.');
    }
    const pct = this.calculateCurrentPercentage(attended, conducted);
    const minReq = sub.minAttendancePercentage ?? existing.minAttendancePercentage;

    this.subjects[index] = {
      ...existing,
      ...sub,
      classesAttended: attended,
      classesConducted: conducted,
      minAttendancePercentage: minReq,
      currentAttendancePercentage: pct,
      currentStatus: pct >= minReq ? 'SAFE' : 'AT_RISK'
    };
    this.saveState();
    return this.subjects[index];
  }

  public deleteSubject(id: number) {
    this.subjects = this.subjects.filter(s => s.id !== id);
    this.timetable = this.timetable.filter(t => t.subjectId !== id);
    this.saveState();
  }

  // Timetable
  public getTimetable(): TimetableEntry[] {
    return this.timetable.map(t => {
      const sub = this.subjects.find(s => s.id === t.subjectId);
      return {
        ...t,
        subjectName: sub ? sub.name : 'Unknown',
        subjectCode: sub ? sub.code : '',
        color: sub ? sub.color : '#94a3b8'
      };
    });
  }

  public assignSlot(day: DayOfWeekEnum, period: number, time: string, subjectId: number): TimetableEntry {
    const existingIndex = this.timetable.findIndex(t => t.dayOfWeek === day && t.periodIndex === period);
    const sub = this.subjects.find(s => s.id === subjectId);
    if (!sub) throw new Error('Subject not found');

    if (existingIndex >= 0) {
      this.timetable[existingIndex] = {
        ...this.timetable[existingIndex],
        slotTime: time,
        subjectId,
        subjectName: sub.name,
        subjectCode: sub.code,
        color: sub.color
      };
      this.saveState();
      return this.timetable[existingIndex];
    } else {
      const newId = this.timetable.length > 0 ? Math.max(...this.timetable.map(t => t.id || 0)) + 1 : 1;
      const created: TimetableEntry = {
        id: newId,
        dayOfWeek: day,
        periodIndex: period,
        slotTime: time,
        subjectId,
        subjectName: sub.name,
        subjectCode: sub.code,
        color: sub.color
      };
      this.timetable.push(created);
      this.saveState();
      return created;
    }
  }

  public removeSlot(day: DayOfWeekEnum, period: number) {
    this.timetable = this.timetable.filter(t => !(t.dayOfWeek === day && t.periodIndex === period));
    this.saveState();
  }

  // Holidays
  public getHolidays(): Holiday[] {
    return this.holidays;
  }

  public addHoliday(date: string, name: string, desc?: string): Holiday {
    const newId = this.holidays.length > 0 ? Math.max(...this.holidays.map(h => h.id || 0)) + 1 : 1;
    const h: Holiday = { id: newId, date, name, description: desc };
    this.holidays.push(h);
    this.saveState();
    return h;
  }

  public deleteHoliday(id: number) {
    this.holidays = this.holidays.filter(h => h.id !== id);
    this.saveState();
  }

  // Trips & Projection Logic
  private getDayOfWeekName(date: Date): DayOfWeekEnum {
    const days: DayOfWeekEnum[] = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    return days[date.getDay()];
  }

  public countMissedClasses(departure: string, returnDate: string): Map<number, number> {
    const counts = new Map<number, number>();
    const holidaySet = new Set(this.holidays.map(h => h.date));

    const cur = new Date(departure + 'T00:00:00');
    const end = new Date(returnDate + 'T00:00:00');

    while (cur <= end) {
      const dateStr = cur.toISOString().split('T')[0];
      if (!holidaySet.has(dateStr)) {
        const dayName = this.getDayOfWeekName(cur);
        const daySlots = this.timetable.filter(t => t.dayOfWeek === dayName);
        for (const slot of daySlots) {
          counts.set(slot.subjectId, (counts.get(slot.subjectId) || 0) + 1);
        }
      }
      cur.setDate(cur.getDate() + 1);
    }
    return counts;
  }

  public calculateTripProjection(name: string, departure: string, returnDate: string, notes?: string, save: boolean = false): TripProjectionResponse {
    if (new Date(returnDate) < new Date(departure)) {
      throw new Error('Return date cannot be earlier than departure date.');
    }

    const start = new Date(departure + 'T00:00:00');
    const end = new Date(returnDate + 'T00:00:00');
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const durationDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

    const missedMap = this.countMissedClasses(departure, returnDate);
    const subjects = this.getSubjects();

    const projections: SubjectProjectionResult[] = [];
    let safeCount = 0;
    let atRiskCount = 0;
    let totalMissed = 0;
    let bottleneck: SubjectProjectionResult | null = null;

    for (const sub of subjects) {
      const missed = missedMap.get(sub.id) || 0;
      totalMissed += missed;

      const currentPct = this.calculateCurrentPercentage(sub.classesAttended, sub.classesConducted);
      const projectedPct = this.calculateProjectedPercentage(sub.classesAttended, sub.classesConducted, missed);
      const status: RiskStatus = (projectedPct + 1e-9 >= sub.minAttendancePercentage) ? 'SAFE' : 'AT_RISK';
      const margin = projectedPct - sub.minAttendancePercentage;

      if (status === 'SAFE') {
        safeCount++;
      } else {
        atRiskCount++;
      }

      let explanation = '';
      if (missed === 0) {
        explanation = `${sub.name} has no classes scheduled during this trip. Attendance holds at ${currentPct.toFixed(1)}%.`;
      } else if (status === 'SAFE') {
        explanation = `Missing ${missed} class(es) lowers attendance from ${currentPct.toFixed(1)}% to ${projectedPct.toFixed(1)}% (still ${(projectedPct - sub.minAttendancePercentage).toFixed(1)}% above the ${sub.minAttendancePercentage}% threshold).`;
      } else {
        explanation = `CRITICAL: Missing ${missed} class(es) drops attendance from ${currentPct.toFixed(1)}% to ${projectedPct.toFixed(1)}%, which is ${(sub.minAttendancePercentage - projectedPct).toFixed(1)}% BELOW the minimum required ${sub.minAttendancePercentage}%!`;
      }

      const res: SubjectProjectionResult = {
        subjectId: sub.id,
        subjectName: sub.name,
        subjectCode: sub.code,
        color: sub.color,
        classesConductedBefore: sub.classesConducted,
        classesAttended: sub.classesAttended,
        currentAttendancePercentage: currentPct,
        expectedMissedClasses: missed,
        classesConductedAfter: sub.classesConducted + missed,
        projectedAttendancePercentage: projectedPct,
        requiredAttendancePercentage: sub.minAttendancePercentage,
        status,
        riskMargin: margin,
        explanation
      };

      projections.push(res);

      if (!bottleneck || margin < bottleneck.riskMargin) {
        bottleneck = res;
      }
    }

    const overallStatus: RiskStatus = atRiskCount > 0 ? 'AT_RISK' : 'SAFE';

    let summaryMessage = '';
    let recommendationMessage = '';

    if (overallStatus === 'SAFE') {
      summaryMessage = `Trip is Safe! All ${safeCount} subjects maintain or exceed minimum attendance.`;
      recommendationMessage = 'Your travel dates fit comfortably within your attendance margin. Safe travels!';
    } else {
      summaryMessage = `HIGH RISK WARNING: ${atRiskCount} subject(s) will fall below minimum attendance!`;
      recommendationMessage = `Shorten your journey or adjust dates. Bottleneck '${bottleneck?.subjectName}' will drop to ${bottleneck?.projectedAttendancePercentage.toFixed(1)}% (minimum requirement: ${bottleneck?.requiredAttendancePercentage}%).`;
    }

    const response: TripProjectionResponse = {
      tripName: name || 'Planned Trip',
      departureDate: departure,
      returnDate,
      durationDays,
      overallStatus,
      safeSubjectsCount: safeCount,
      atRiskSubjectsCount: atRiskCount,
      totalMissedClasses: totalMissed,
      subjectProjections: projections,
      bottleneckSubjectName: bottleneck?.subjectName,
      mostAffectedSubjects: projections.filter(p => p.expectedMissedClasses > 0).sort((a, b) => a.riskMargin - b.riskMargin).map(p => p.subjectName),
      summaryMessage,
      recommendationMessage
    };

    if (save) {
      const newTripId = this.trips.length > 0 ? Math.max(...this.trips.map(t => t.id)) + 1 : 1;
      const savedTrip: Trip = {
        id: newTripId,
        tripName: response.tripName,
        departureDate: departure,
        returnDate,
        notes,
        overallStatus,
        safeSubjectsCount: safeCount,
        atRiskSubjectsCount: atRiskCount,
        totalClassesMissed: totalMissed,
        projectionDetailsJson: JSON.stringify(projections),
        createdAt: new Date().toISOString()
      };
      this.trips.unshift(savedTrip);
      this.saveState();
      response.savedTripId = newTripId;
    }

    return response;
  }

  public findMaxSafeDuration(departure: string, horizon: number = 30): MaxSafeDurationResponse {
    const subjects = this.getSubjects();
    if (subjects.length === 0) {
      const dep = new Date(departure + 'T00:00:00');
      dep.setDate(dep.getDate() + horizon - 1);
      return {
        departureDate: departure,
        recommendedReturnDate: dep.toISOString().split('T')[0],
        maxSafeDays: horizon,
        departureDateSafe: true,
        reachedHorizonWithoutFailure: true,
        searchHorizonDays: horizon,
        explanation: 'No subjects are tracked. Travel is unconstrained.',
        noClassesMissed: true,
        projectedResultsAtRecommendedDate: []
      };
    }

    let lastSafeDate = '';
    let lastSafeProjections: SubjectProjectionResult[] = [];
    let limiter = '';

    for (let day = 0; day < horizon; day++) {
      const cur = new Date(departure + 'T00:00:00');
      cur.setDate(cur.getDate() + day);
      const candDate = cur.toISOString().split('T')[0];

      const proj = this.calculateTripProjection('Test', departure, candDate, '', false);
      if (proj.overallStatus === 'SAFE') {
        lastSafeDate = candDate;
        lastSafeProjections = proj.subjectProjections;
      } else {
        if (day === 0) {
          return {
            departureDate: departure,
            recommendedReturnDate: departure,
            maxSafeDays: 0,
            departureDateSafe: false,
            reachedHorizonWithoutFailure: false,
            searchHorizonDays: horizon,
            limitingSubjectName: proj.bottleneckSubjectName,
            explanation: `CRITICAL: Even missing classes on the departure date (${departure}) drops '${proj.bottleneckSubjectName}' below threshold. Safe duration is 0 days.`,
            noClassesMissed: false,
            projectedResultsAtRecommendedDate: proj.subjectProjections
          };
        }
        limiter = proj.bottleneckSubjectName || 'Subject';
        break;
      }
    }

    const start = new Date(departure + 'T00:00:00');
    const end = new Date(lastSafeDate + 'T00:00:00');
    const safeDays = Math.ceil((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)) + 1;
    const reached = safeDays === horizon;

    return {
      departureDate: departure,
      recommendedReturnDate: lastSafeDate,
      maxSafeDays: safeDays,
      departureDateSafe: true,
      reachedHorizonWithoutFailure: reached,
      searchHorizonDays: horizon,
      limitingSubjectName: limiter,
      explanation: reached 
        ? `Safe across the entire ${horizon}-day horizon up to ${lastSafeDate}.`
        : `Safe travel duration is ${safeDays} days through ${lastSafeDate}. Extending one more day breaches attendance in '${limiter}'.`,
      noClassesMissed: lastSafeProjections.every(p => p.expectedMissedClasses === 0),
      projectedResultsAtRecommendedDate: lastSafeProjections
    };
  }

  public getDashboardSummary(): DashboardSummary {
    const subjects = this.getSubjects();
    const safeCount = subjects.filter(s => s.currentStatus === 'SAFE').length;
    const avg = subjects.length > 0 ? subjects.reduce((sum, s) => sum + s.currentAttendancePercentage, 0) / subjects.length : 0;
    
    const today = new Date().toISOString().split('T')[0];
    const upcoming = this.trips.find(t => t.departureDate >= today);

    return {
      totalSubjects: subjects.length,
      averageAttendance: avg,
      safeSubjectsCount: safeCount,
      atRiskSubjectsCount: subjects.length - safeCount,
      upcomingTrip: upcoming,
      subjects,
      recentTrips: this.trips.slice(0, 5)
    };
  }

  public getTrips(): Trip[] {
    return this.trips;
  }

  public deleteTrip(id: number) {
    this.trips = this.trips.filter(t => t.id !== id);
    this.saveState();
  }
}

export const localEngine = new LocalCalculationEngine();
