# REST API reference

Base URL: `http://localhost:8080/api`. JSON requests use `Content-Type: application/json`. Protected routes use `Authorization: Bearer <JWT>`.

## Authentication

- `POST /auth/register` — `{ "fullName": "Asha Patil", "email": "asha@example.com", "password": "at-least-8-chars" }`. Public registration always assigns `STUDENT`.
- `POST /auth/login` — `{ "email": "asha@example.com", "password": "..." }`.
- `GET /auth/me` — current authenticated user summary and role.

The login/register response includes `token`, `message`, `id`, `fullName`, `email`, and `role`.

## Student profile and directory

- `GET /students/me`
- `POST /students/me`
- `PUT /students/me`
- `GET /students` — admin only.

Profile payload includes `fullName`, `email`, `phone`, `collegeName`, `degree`, `branch`, `graduationYear`, `academicYear`, `cgpa`, `skills`, `currentLocation`, `linkedinUrl`, `githubUrl`, and `portfolioUrl`.

## Companies and jobs

- `GET /companies`, `GET /companies/{id}` — authenticated users.
- `POST /companies`, `PUT /companies/{id}`, `DELETE /companies/{id}` — admin only.
- `GET /jobs?search=java` — active jobs; search checks title, company, location and skills.
- `GET /jobs?includeInactive=true` — administrator job list.
- `GET /jobs/{id}` — authenticated users.
- `POST /jobs`, `PUT /jobs/{id}`, `DELETE /jobs/{id}` — admin only.

Job payload: `{ "companyId": 1, "title": "Graduate Engineer Trainee", "description": "...", "location": "Pune", "salaryLpa": 4.5, "eligibility": "Final-year students", "requiredSkills": "Java, SQL", "deadline": "2026-12-15", "active": true }`.

## Applications

- `POST /job-applications` — student only. Example: `{ "jobId": 3, "notes": "Available for the Pune location" }`. The server looks up the job, derives company and role, verifies it is active and before deadline, then derives the student from the JWT.
- `GET /job-applications` — students see only their own applications; admins can see all and optionally filter by `studentId`, `companyId`, and `status`.
- `GET /job-applications/{id}` — owner or admin.
- `PUT /job-applications/{id}` — admin only, example `{ "status": "SHORTLISTED", "notes": "Next round" }`.
- `DELETE /job-applications/{id}` — owner can withdraw while status is `APPLIED`; admin can remove records.

Statuses: `APPLIED`, `UNDER_REVIEW`, `SHORTLISTED`, `INTERVIEW`, `REJECTED`, `SELECTED`.

## Dashboard and resume

- `GET /dashboard/stats` — admin statistics.
- `POST /resumes/me` — multipart form field `file`; PDF or DOCX, 5 MB max.
- `GET /resumes/me` — current user's resume metadata.
- `GET /resumes/me/download` — authenticated download.
- `DELETE /resumes/me` — delete own resume.

## Error responses

Validation and expected application errors return HTTP status codes and a JSON body with `status`, `message`, and `timestamp`. A protected API call without a valid JWT returns 401. An authenticated role without permission receives 403.
