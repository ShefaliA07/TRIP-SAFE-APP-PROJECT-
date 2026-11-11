#!/usr/bin/env bash
# ==============================================================================
# TripSafe — Application Launch Script
# ==============================================================================

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "=================================================================="
echo "  🛡️  TripSafe: Pre-Travel Attendance Risk Projector for Students"
echo "=================================================================="
echo "Project Location: ${PROJECT_DIR}"
echo ""

echo "Available run modes:"
echo " 1) Instant Web Application (Zero-setup, runs immediately via Python 3)"
echo " 2) Java Spring Boot Backend (Requires JDK 17+ and Maven)"
echo " 3) React TypeScript Frontend (Requires Node.js and npm)"
echo ""

# Launch web-app by default if python3 is available
if command -v python3 &>/dev/null; then
    echo "Starting TripSafe Web Application on http://localhost:3000 ..."
    cd "${PROJECT_DIR}/web-app" && python3 serve.py
else
    echo "Python 3 not found. Please open ${PROJECT_DIR}/web-app/index.html directly in your browser."
fi
