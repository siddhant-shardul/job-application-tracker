# CampusTrack implementation and review report

## Scope of the supplied source

The supplied archive contained a Spring Boot backend only; it had no React application, no job-posting entity/API, and no resume feature. A React/Vite frontend and the missing job/resume backend modules have now been added while retaining the supplied Java, Spring Boot, Spring Security, JPA/Hibernate, PostgreSQL and JWT stack.

## Changes made

### Backend

- Added `Job` entity, request/response DTOs, repository, service and REST controller. Supports job creation, editing, deletion, active/closed state, deadline, eligibility, location, salary and skill search.
- Added authenticated job reads and ADMIN-only job writes.
- Updated application creation to resolve the job posting server-side, derive company/title from the stored job record, reject closed or expired roles, derive the student from the JWT, and retain duplicate-application checks.
- Added `Resume` metadata entity, repository, service and controller. Resume binaries are kept on the backend filesystem using server-generated names; users cannot supply storage paths. Upload is restricted to PDF/DOCX, 5 MB and basic PDF/ZIP signature checks. Download is authenticated and attachment-based.
- Added student academic year, CGPA and skills fields with backend validation.
- Added `/api/auth/me` to restore the current session role on frontend reload. Login/register responses now also include identity and role fields.
- Added admin student-directory endpoint, job totals and under-review count to admin dashboard statistics.
- Added `EntityGraph` to reduce repeated lazy-loading queries for jobs/applications and sorted application histories newest-first.
- Tightened role matchers for job/company writes, dashboard, resume, student directory and application status changes.
- Added multipart limits and a 413 response for oversized uploads.
- Added H2 test-profile configuration so the Spring context test does not require a local PostgreSQL server.

### Frontend

- Created a React 19 + Vite frontend with React Router and reusable UI components.
- Built a responsive public landing page, login and student registration pages.
- Added authenticated app layout, student/admin navigation, session restore and sign-out.
- Built student dashboard, job search/list/detail, application history, student profile and resume upload/download screens.
- Built admin dashboard, company CRUD, job-posting CRUD, student directory and application status management.
- Added loading, empty, success/error, search, status badge and confirmation states.
- Added responsive CSS for desktop/tablet/mobile with a restrained navy/teal visual style intended to look like a credible student-built campus placement project.
- Dashboard statistics and lists are sourced from API responses; no sample records or seeded users are inserted.

### Documentation and project tooling

- Replaced backend-only README with full-stack setup, admin provisioning, production build and limitation instructions.
- Added frontend `.env.example`, Postman collection, expanded API overview and database design notes.
- Updated `.gitignore` for frontend build outputs, dependencies, private environment files and uploaded resumes.

## Security review notes

- Public registration always creates STUDENT; administrator promotion is a trusted SQL/DBA operation documented in README.
- Passwords continue to use BCrypt.
- Student profile and resume operations derive the account from the authenticated JWT.
- Application ownership is checked server-side; client-provided `studentId` is ignored.
- Student application creation resolves an existing active job and checks deadline, rather than trusting arbitrary client-provided company/role text.
- Students cannot create companies/jobs, read the admin dashboard, enumerate student profiles, or update application statuses.
- Resume files are not exposed as static resources and use generated storage names.
- JWT secret and database password remain environment-based.

## Verification performed in this environment

| Check | Result | Notes |
|---|---|---|
| Original ZIP inspection | Passed | Confirmed original project was backend-only |
| Added source/config/document file readability | Passed | Project tree inspected after modifications |
| Java source syntax | Passed | JDK compiler parser parsed all 48 Java source files with zero syntax errors; this is a syntax parse, not a dependency-resolved compile |
| Local backend imports | Passed | Script found no missing internal `com.siddhant.backend` imports |
| Postman collection JSON | Passed | Parsed as valid JSON |
| Frontend package JSON | Passed | Parsed as valid JSON |
| JavaScript config/API syntax | Passed | `node --check` |
| JSX/JS syntax parsing | Passed | TypeScript compiler parser with JSX enabled and dependency resolution disabled |
| Maven compile/tests | Not run | Maven Wrapper could not download Maven distribution from Maven Central in this environment |
| npm install / Vite production build | Not run | npm install stalled due package registry/network access in this environment; dependencies were not available locally |
| PostgreSQL connection/startup | Not run | No local PostgreSQL client/service credentials available |
| Browser end-to-end workflows | Not run | Frontend dependencies and browser runtime were unavailable |
| Resume upload/download against server | Not run | Requires running Spring Boot + PostgreSQL |

Do not interpret source parsing as a successful production build. Run the Maven and npm commands in README on a machine with access to Maven Central/npm and then run the application against a local PostgreSQL database.

## Known limitations / next sensible improvements

1. This implementation has not been runtime-verified end-to-end in this isolated environment. Maven and npm dependency downloads failed here.
2. Lists are currently unpaginated. Add Spring Data pagination once the student/application dataset becomes large.
3. Resume validation checks extension, content type, size and basic PDF/ZIP signatures. Add antivirus scanning and stronger DOCX package inspection before handling untrusted public uploads at scale.
4. Resume files need a private persistent volume and backup policy in production; the current default is a local `./uploads/resumes` folder.
5. Applications preserve role/company text but do not persist a direct foreign key to the job posting. This keeps historical application descriptions stable after a posting is edited/deleted, but does not provide a full immutable job snapshot.
6. The admin role must be provisioned manually; no public admin creation route exists by design.
7. CORS is configured for localhost development. Update the allowed origins before deploying frontend/backend on separate production domains.
8. There is no automated browser E2E suite or API integration suite yet. The H2 context test covers Spring startup wiring only.
9. For production, add versioned database migrations, rate limiting, security headers, monitoring, centralized logs and an explicit retention policy for student resumes.

## Overall assessment

The project is now a coherent full-stack portfolio application in source structure and feature coverage. It is suitable for local setup and further student-led development. It should be described as **implementation-complete but not deployment-verified** until the Maven build, npm build, PostgreSQL startup and student/admin workflows pass on a machine with dependency/network access.
