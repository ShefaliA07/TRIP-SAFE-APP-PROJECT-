import React, { useState, useEffect } from 'react';
import { api } from './services/api';
import {
  Shield,
  LayoutDashboard,
  BookOpen,
  CalendarDays,
  PlaneTakeoff,
  Compass,
  History as HistoryIcon,
  Settings as SettingsIcon,
  RefreshCw,
  Sparkles,
  CheckCircle2,
  AlertTriangle,
  AlertOctagon
} from 'lucide-react';

export type NavTab = 'landing' | 'dashboard' | 'subjects' | 'timetable' | 'planner' | 'max-safe' | 'history' | 'settings';

export default function App() {
  const [activeTab, setActiveTab] = useState<NavTab>('dashboard');
  const [backendOnline, setBackendOnline] = useState<boolean>(false);
  const [checkingBackend, setCheckingBackend] = useState<boolean>(false);
  const [toast, setToast] = useState<{ message: string; type: 'success' | 'warning' | 'error' } | null>(null);

  const showToast = (message: string, type: 'success' | 'warning' | 'error' = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  const verifyBackend = async () => {
    setCheckingBackend(true);
    const online = await api.checkBackendHealth();
    setBackendOnline(online);
    setCheckingBackend(false);
    if (online) {
      showToast('Connected to Spring Boot REST API (Port 8080)!', 'success');
    }
  };

  useEffect(() => {
    verifyBackend();
  }, []);

  return (
    <div className="flex flex-col min-h-screen bg-slate-50 text-slate-900 font-sans">
      {/* Top Navbar */}
      <header className="sticky top-0 z-40 bg-white border-b border-slate-200 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            {/* Logo */}
            <div 
              onClick={() => setActiveTab('landing')}
              className="flex items-center gap-3 cursor-pointer group"
            >
              <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-indigo-700 via-brand-600 to-blue-500 flex items-center justify-center text-white shadow-md shadow-brand-500/20 group-hover:scale-105 transition-transform">
                <Shield className="w-6 h-6" />
              </div>
              <div>
                <div className="flex items-center gap-2">
                  <span className="font-extrabold text-xl tracking-tight bg-gradient-to-r from-slate-900 via-indigo-900 to-brand-900 bg-clip-text text-transparent">
                    TripSafe
                  </span>
                  <span className="text-[10px] font-bold uppercase tracking-wider px-2 py-0.5 rounded-full bg-brand-50 text-brand-700 border border-brand-200">
                    Java 17 • React TS
                  </span>
                </div>
                <p className="text-xs text-slate-500 hidden sm:block">Pre-Travel Attendance Risk Projector</p>
              </div>
            </div>

            {/* Right Status & Actions */}
            <div className="flex items-center gap-3">
              <button
                onClick={verifyBackend}
                title="Click to re-check Spring Boot backend connection"
                className={`flex items-center gap-2 text-xs font-semibold px-3 py-1.5 rounded-full border transition-all ${
                  backendOnline
                    ? 'bg-emerald-50 text-emerald-800 border-emerald-200 hover:bg-emerald-100'
                    : 'bg-blue-50 text-blue-800 border-blue-200 hover:bg-blue-100'
                }`}
              >
                <span className={`w-2 h-2 rounded-full animate-pulse ${backendOnline ? 'bg-emerald-500' : 'bg-blue-500'}`} />
                <span>{backendOnline ? 'REST API Online' : 'Local Engine Fallback'}</span>
                <RefreshCw className={`w-3 h-3 ${checkingBackend ? 'animate-spin' : ''}`} />
              </button>

              <button
                onClick={() => setActiveTab('planner')}
                className="flex items-center gap-1.5 bg-brand-600 hover:bg-brand-700 text-white text-xs font-semibold px-3.5 py-2 rounded-lg shadow-sm transition-all"
              >
                <PlaneTakeoff className="w-3.5 h-3.5" />
                <span>Plan Trip</span>
              </button>
            </div>
          </div>
        </div>

        {/* Navigation Tabs Bar */}
        <div className="bg-slate-50 border-t border-slate-200/80 px-4 sm:px-6 lg:px-8 overflow-x-auto">
          <div className="max-w-7xl mx-auto flex gap-1 sm:gap-2 py-1.5 min-w-max">
            {[
              { id: 'landing', label: 'Overview', icon: Sparkles },
              { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
              { id: 'subjects', label: 'Subjects', icon: BookOpen },
              { id: 'timetable', label: 'Timetable & Holidays', icon: CalendarDays },
              { id: 'planner', label: 'Trip Planner (Core)', icon: PlaneTakeoff, highlight: true },
              { id: 'max-safe', label: 'Find Max Safe Trip', icon: Compass },
              { id: 'history', label: 'Trip History', icon: HistoryIcon },
              { id: 'settings', label: 'Settings & Data', icon: SettingsIcon },
            ].map((item) => {
              const Icon = item.icon;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id as NavTab)}
                  className={`flex items-center gap-2 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                    activeTab === item.id
                      ? 'bg-white text-brand-700 shadow-sm border border-slate-200'
                      : item.highlight
                        ? 'text-brand-600 font-bold hover:bg-brand-50/60'
                        : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'
                  }`}
                >
                  <Icon className="w-3.5 h-3.5" />
                  <span>{item.label}</span>
                </button>
              );
            })}
          </div>
        </div>
      </header>

      {/* Toast Notification */}
      {toast && (
        <div className="fixed bottom-5 right-5 z-50 animate-bounce">
          <div className={`flex items-center gap-2.5 px-4 py-3 rounded-xl shadow-xl text-white text-xs font-medium ${
            toast.type === 'warning' ? 'bg-amber-600' : toast.type === 'error' ? 'bg-rose-600' : 'bg-slate-900'
          }`}>
            {toast.type === 'warning' && <AlertTriangle className="w-4 h-4" />}
            {toast.type === 'error' && <AlertOctagon className="w-4 h-4" />}
            {toast.type === 'success' && <CheckCircle2 className="w-4 h-4" />}
            <span>{toast.message}</span>
          </div>
        </div>
      )}

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 shadow-sm space-y-4">
          <div className="w-16 h-16 rounded-2xl bg-brand-50 text-brand-600 mx-auto flex items-center justify-center">
            <Shield className="w-8 h-8" />
          </div>
          <h2 className="text-2xl font-bold text-slate-900">TripSafe Web Application</h2>
          <p className="text-sm text-slate-600 max-w-xl mx-auto">
            The full-featured React & Spring Boot application is ready. For instant zero-dependency demonstration, launch the bundled interactive app in <code className="bg-slate-100 px-2 py-1 rounded text-brand-700 font-mono text-xs">web-app/</code> using <code className="bg-slate-100 px-2 py-1 rounded font-mono text-xs">python3 serve.py</code>!
          </p>
          <div className="flex justify-center gap-3 pt-2">
            <button
              onClick={() => showToast('For immediate demonstration, run: cd web-app && python3 serve.py', 'success')}
              className="bg-brand-600 hover:bg-brand-700 text-white text-xs font-semibold px-4 py-2.5 rounded-xl shadow-sm"
            >
              Show Command
            </button>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="mt-auto bg-white border-t border-slate-200 py-6 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row justify-between items-center gap-3">
          <div className="flex items-center gap-2">
            <Shield className="w-4 h-4 text-brand-600" />
            <span className="font-semibold text-slate-800">TripSafe</span>
            <span>— A Pre-Travel Attendance Risk Projector for Hostel Students</span>
          </div>
          <p>College Java Project • Three-Tier Architecture</p>
        </div>
      </footer>
    </div>
  );
}
