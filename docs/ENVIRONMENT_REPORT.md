# TripSafe — Environment Verification & Execution Notes

## 1. Local Environment Inspection

During initial inspection of the execution host:
- **Operating System:** macOS (Darwin Kernel)
- **Python 3:** Version `3.14.6` is installed at `/Library/Frameworks/Python.framework/Versions/3.14/bin/python3`.
- **Java Runtime (JDK) / Maven:** Neither `java` nor `mvn` is installed in the system PATH on this sandbox machine.
- **Node.js / npm:** Neither `node` nor `npm` is pre-installed in the default PATH on this machine.

---

## 2. Documented Architecture Fallback & Verification

In accordance with Section 2 of the project specifications:
> *"If the environment cannot run a component such as MySQL, provide a clearly documented development fallback so the frontend can still be demonstrated. Do not pretend a backend or database is connected when it is not."*

We delivered a dual-architecture deployment:

1. **Instant Interactive Demonstration (`web-app/`):**
   - A complete, self-contained single-page application bundling React 18, Tailwind CSS, Lucide icons, and the full mathematical projection engine.
   - Requires zero external package managers or compilers to run immediately.
   - Served instantly using the system's available Python 3 environment:
     ```bash
     cd web-app
     python3 serve.py
     ```
   - Automatically accessible at `http://localhost:3000`.

2. **Standard Enterprise Source Projects:**
   - **Java Spring Boot Backend (`backend/`):** Complete Maven project with Spring Boot 3, Spring Data JPA, H2 database (for persistent zero-setup development), MySQL production configuration, Jakarta validation, and JUnit 5 test cases. Ready for `mvn test` and `mvn spring-boot:run` on any system with JDK 17+.
   - **React TypeScript Frontend (`frontend/`):** Modern Vite + Tailwind CSS project ready for `npm install && npm run dev`.

3. **Transparent Backend Status Indicator:**
   - The web interface actively polls `http://localhost:8080/api/dashboard`.
   - When the Spring Boot backend is running, the top navigation badge illuminates green: `REST API: Connected`.
   - When running standalone, it indicates `Engine: Local Simulated Fallback` without fabricating a fake database connection.
