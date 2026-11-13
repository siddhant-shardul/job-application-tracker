# CampusTrack — Job Application & Placement Tracker

A straightforward full-stack placement management project built with **Java 21, Spring Boot, Spring Security, JWT, Spring Data JPA, PostgreSQL, React, Vite and CSS**. It is intentionally kept understandable and practical for a BCA/BCS/MCA student portfolio rather than introducing unnecessary enterprise infrastructure.

## Project structure

```text
job-application-tracker-main/
├── backend/                 Spring Boot REST API
│   ├── src/main/java/       Controllers, services, entities, DTOs and security
│   ├── src/main/resources/  PostgreSQL and upload configuration
│   └── src/test/            H2-backed Spring context test
├── frontend/                React + Vite application
│   ├── src/components/      Layout, navigation and shared UI
│   ├── src/context/         Authentication state
│   ├── src/pages/           Public, student and admin pages
│   └── src/lib/             API client and format helpers
├── docs/                    API, database and review notes
├── postman/                 Postman collection
└── .env.example             Backend environment template
```

## Features

### Student
- Student registration and JWT login.
- Student profile with education, graduation year, CGPA, skills and contact links.
- PDF/DOCX resume upload, private download and replacement.
- Browse and search open job postings.
- Apply to an active job and prevent repeat applications to the same company/role.
- View application history/status and withdraw an application while it is still `APPLIED`.
- Personal dashboard with application counts and recent opportunities.

### Administrator
- Role-protected dashboard statistics from database records.
- Create, update, search and delete companies.
- Create, update and delete job postings, set deadlines and close roles.
- View registered student profiles.
- View applications and update their status (`APPLIED`, `UNDER_REVIEW`, `SHORTLISTED`, `INTERVIEW`, `REJECTED`, `SELECTED`).

## Prerequisites

- Java **21**
- PostgreSQL **14 or newer**
- Node.js **20 or newer** and npm **10 or newer**
- Maven Wrapper is included; a separate Maven installation is not required.
- IntelliJ IDEA / VS Code recommended.

## 1. Database setup

Start PostgreSQL, then create the database:

```sql
CREATE DATABASE placement_tracker;
```

Use a PostgreSQL account that has permission to create and alter tables in this database. The application uses Hibernate `ddl-auto=update` by default: it creates missing tables and adds mapped schema changes without issuing a table-drop/reset command. Back up the database before deploying schema changes. For a production environment, use reviewed versioned migrations and set `DDL_AUTO=validate` after the schema has been provisioned.

## 2. Configure and run the backend

Set the variables in `.env.example` in your terminal or IDE Run Configuration. Spring Boot does **not** automatically load a root `.env` file.

Linux/macOS example:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/placement_tracker
export DB_USERNAME=postgres
export DB_PASSWORD='your-local-postgres-password'
export JWT_SECRET="$(openssl rand -base64 48)"
export JWT_EXPIRATION_MS=86400000
export UPLOAD_DIRECTORY=./uploads/resumes
```

Windows PowerShell example:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/placement_tracker"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-local-postgres-password"
$env:JWT_SECRET="replace-with-a-random-secret-of-at-least-32-characters"
$env:JWT_EXPIRATION_MS="86400000"
$env:UPLOAD_DIRECTORY="./uploads/resumes"
```

`JWT_SECRET` must contain at least 32 UTF-8 bytes. Use a newly generated secret locally; never commit it. Then:

```bash
cd backend
./mvnw spring-boot:run
```

Windows:

```cmd
cd backend
mvnw.cmd spring-boot:run
```

The API listens at `http://localhost:8080`. The `POST /api/auth/register` and `POST /api/auth/login` routes are public. The application will create/update its JPA schema after it connects to PostgreSQL.

## 3. Provision the first administrator

Public registration deliberately always creates a `STUDENT`. To create your first admin, register an account through the app, then run this command in a trusted PostgreSQL session (replace the email with the account you registered):

```sql
UPDATE users
SET role = 'ADMIN'
WHERE email = 'placement.admin@example.com';
```

Verify the change:

```sql
SELECT id, full_name, email, role
FROM users
WHERE email = 'placement.admin@example.com';
```

Sign out and sign in again so the new role is reflected in the session. Do not expose a public “make me admin” endpoint.

## 4. Configure and run the React frontend

In a second terminal:

```bash
cd frontend
```

Copy `.env.example` to `.env` (or create `.env`) and set:

```env
VITE_API_URL=http://localhost:8080/api
```

Install packages and start Vite:

```bash
npm install
npm run dev
```

Open `http://localhost:5173`.

The frontend supports student and administrator routes. Register a student account to try the student workflow. Provision an administrator account using the SQL above to test placement-management pages.

## 5. Build for production

Build the frontend:

```bash
cd frontend
npm install
npm run build
```

The static site is generated in `frontend/dist`. Set `VITE_API_URL` to the public HTTPS API base **before** building. Serve `dist` through Nginx, Apache or a static hosting service, and configure the host to fall back to `index.html` for React Router paths.

Build and run the backend:

```bash
cd backend
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

In production set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS`, `UPLOAD_DIRECTORY`, `SERVER_PORT` and `DDL_AUTO=validate` in the deployment environment. Use HTTPS, a managed PostgreSQL database, a secret manager, a private persistent volume for `UPLOAD_DIRECTORY`, regular backups and a restrictive CORS origin list. The current CORS allow-list is intended for local development (`localhost:5173` and `localhost:3000`); change it in `SecurityConfig` for your deployed frontend domain.

## 6. API overview

API base: `http://localhost:8080/api`. Send `Authorization: Bearer <token>` on protected requests.

| Area | Endpoints | Access |
|---|---|---|
| Auth | `POST /auth/register`, `POST /auth/login`, `GET /auth/me` | Register/login public; `/me` authenticated |
| Student profile | `GET/POST/PUT /students/me`, `GET /students` | Student owns `/me`; admin can list |
| Companies | `GET /companies`, `GET /companies/{id}`, `POST/PUT/DELETE /companies` | Reads authenticated; writes admin |
| Jobs | `GET /jobs`, `GET /jobs/{id}`, `POST/PUT/DELETE /jobs` | Reads authenticated; writes admin |
| Applications | `GET/POST /job-applications`, `GET/PUT/DELETE /job-applications/{id}` | Student-owned access; admin management/status |
| Dashboard | `GET /dashboard/stats` | Admin |
| Resume | `POST/GET/DELETE /resumes/me`, `GET /resumes/me/download` | Authenticated student only |

The application service derives the student identity from the JWT. A submitted `studentId` is not used to assign an application. New application submissions are checked against a real active job posting and its deadline.

See `docs/API_OVERVIEW.md` and the Postman collection in `postman/` for example payloads.

## 7. Automated checks

Backend tests use an in-memory H2 database and the `test` Spring profile; no local PostgreSQL instance is needed for the context test:

```bash
cd backend
./mvnw test
```

Frontend production build:

```bash
cd frontend
npm install
npm run build
```

The frontend uses the actual API; it does not ship fake student/application records. A database with no records will display empty states until you register a student, create a profile, and add company/job records as an admin.

## 8. Notes and known limits

- Resume files are stored on the backend filesystem in `UPLOAD_DIRECTORY`, not in PostgreSQL. In production this must be a private persistent volume and backed up.
- Resume upload currently enforces PDF/DOCX extensions, content types and a 5 MB limit. It is not an antivirus scanner; production deployments should add malware scanning and deeper file signature validation.
- Application history stores the company and job-role text on the application row, preserving a readable history if a posting is later edited or removed. The current schema does not keep a separate FK from each historical application to the job-posting row.
- Lists are suitable for a small campus deployment but are not paginated yet. Add pagination when the dataset grows.
- This is a portfolio/learning application, not a certified production placement platform. Perform a deployment security review before using real student data.
