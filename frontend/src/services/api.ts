import {
  Subject,
  TimetableEntry,
  Holiday,
  Trip,
  TripProjectionResponse,
  MaxSafeDurationResponse,
  DashboardSummary,
  DayOfWeekEnum
} from '../types';
import { localEngine } from './localCalculationEngine';

const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
  private backendAvailable: boolean | null = null;

  public async checkBackendHealth(): Promise<boolean> {
    try {
      const controller = new AbortController();
      const id = setTimeout(() => controller.abort(), 1200);
      const res = await fetch(`${API_BASE_URL}/dashboard`, { signal: controller.signal });
      clearTimeout(id);
      this.backendAvailable = res.ok;
      return res.ok;
    } catch {
      this.backendAvailable = false;
      return false;
    }
  }

  public isConnected(): boolean {
    return this.backendAvailable === true;
  }

  // Dashboard
  public async getDashboard(): Promise<DashboardSummary> {
    const isOnline = await this.checkBackendHealth();
    if (isOnline) {
      const res = await fetch(`${API_BASE_URL}/dashboard`);
      return res.json();
    }
    return localEngine.getDashboardSummary();
  }

  // Subjects
  public async getSubjects(): Promise<Subject[]> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/subjects`);
      return res.json();
    }
    return localEngine.getSubjects();
  }

  public async createSubject(sub: Omit<Subject, 'id' | 'currentAttendancePercentage' | 'currentStatus'>): Promise<Subject> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/subjects`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(sub)
      });
      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to create subject');
      }
      return res.json();
    }
    return localEngine.addSubject(sub);
  }

  public async updateSubject(id: number, sub: Partial<Subject>): Promise<Subject> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/subjects/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(sub)
      });
      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to update subject');
      }
      return res.json();
    }
    return localEngine.updateSubject(id, sub);
  }

  public async deleteSubject(id: number): Promise<void> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/subjects/${id}`, { method: 'DELETE' });
      if (!res.ok) throw new Error('Failed to delete subject');
      return;
    }
    localEngine.deleteSubject(id);
  }

  // Timetable
  public async getTimetable(): Promise<TimetableEntry[]> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/timetable`);
      return res.json();
    }
    return localEngine.getTimetable();
  }

  public async assignSlot(day: DayOfWeekEnum, period: number, time: string, subjectId: number): Promise<TimetableEntry> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/timetable/slot`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ dayOfWeek: day, periodIndex: period, slotTime: time, subjectId })
      });
      if (!res.ok) throw new Error('Failed to assign timetable slot');
      return res.json();
    }
    return localEngine.assignSlot(day, period, time, subjectId);
  }

  public async removeSlot(day: DayOfWeekEnum, period: number): Promise<void> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/timetable/slot/${day}/${period}`, { method: 'DELETE' });
      if (!res.ok) throw new Error('Failed to remove slot');
      return;
    }
    localEngine.removeSlot(day, period);
  }

  // Holidays
  public async getHolidays(): Promise<Holiday[]> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/holidays`);
      return res.json();
    }
    return localEngine.getHolidays();
  }

  public async addHoliday(date: string, name: string, desc?: string): Promise<Holiday> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/holidays`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ date, name, description: desc })
      });
      return res.json();
    }
    return localEngine.addHoliday(date, name, desc);
  }

  public async deleteHoliday(id: number): Promise<void> {
    if (this.backendAvailable) {
      await fetch(`${API_BASE_URL}/holidays/${id}`, { method: 'DELETE' });
      return;
    }
    localEngine.deleteHoliday(id);
  }

  // Trips
  public async calculateTripRisk(name: string, departure: string, returnDate: string, notes?: string, save: boolean = false): Promise<TripProjectionResponse> {
    if (this.backendAvailable) {
      const endpoint = save ? `${API_BASE_URL}/trips` : `${API_BASE_URL}/trips/project`;
      const res = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tripName: name, departureDate: departure, returnDate, notes, saveTrip: save })
      });
      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Calculation failed');
      }
      return res.json();
    }
    return localEngine.calculateTripProjection(name, departure, returnDate, notes, save);
  }

  public async findMaxSafeDuration(departure: string, horizon: number = 30): Promise<MaxSafeDurationResponse> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/trips/max-safe-duration`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ departureDate: departure, searchHorizonDays: horizon })
      });
      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to calculate maximum safe duration');
      }
      return res.json();
    }
    return localEngine.findMaxSafeDuration(departure, horizon);
  }

  public async getTrips(): Promise<Trip[]> {
    if (this.backendAvailable) {
      const res = await fetch(`${API_BASE_URL}/trips`);
      return res.json();
    }
    return localEngine.getTrips();
  }

  public async deleteTrip(id: number): Promise<void> {
    if (this.backendAvailable) {
      await fetch(`${API_BASE_URL}/trips/${id}`, { method: 'DELETE' });
      return;
    }
    localEngine.deleteTrip(id);
  }

  // Demo Data Reset / Load
  public async loadDemoData(): Promise<void> {
    if (this.backendAvailable) {
      await fetch(`${API_BASE_URL}/demo/load`, { method: 'POST' });
      return;
    }
    localEngine.resetDemoData();
  }

  public async clearAllData(): Promise<void> {
    if (this.backendAvailable) {
      await fetch(`${API_BASE_URL}/demo/reset`, { method: 'POST' });
      return;
    }
    localEngine.clearAllData();
  }
}

export const api = new ApiService();
