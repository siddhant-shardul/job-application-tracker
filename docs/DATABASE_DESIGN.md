# Database design

The application connects to PostgreSQL using Spring Data JPA/Hibernate. Entity mappings are the current schema source. `spring.jpa.hibernate.ddl-auto=update` is the local default and does not intentionally drop existing tables. Back up data before applying schema changes; production should provision schema through reviewed migrations and use `DDL_AUTO=validate`.

## Main tables

| Table | Purpose | Key relationships |
|---|---|---|
| `users` | Login identity, BCrypt password hash and role | One user can own one student profile |
| `students` | Academic, contact and profile details | Unique `user_id`; unique profile email |
| `companies` | Employer directory | Referenced by jobs and applications |
| `jobs` | Job postings, skills, eligibility, salary and deadline | Many jobs can belong to one company |
| `job_applications` | Student application history and status | Many applications can belong to one student/company |
| `resumes` | Private resume metadata and filesystem storage key | Unique one-to-one student reference |

`job_applications` has a uniqueness constraint on `(student_id, company_id, job_role)` to help prevent duplicate submissions. The service checks duplicates before insert as well. Job applications preserve the company and role values as text on the application row, rather than maintaining a job-posting FK. Resume binary files are stored outside the database; only metadata and a random storage key are persisted in `resumes`.

## Relationships and integrity

- `students.user_id` is unique and non-null.
- `resumes.student_id` is unique and non-null.
- `jobs.company_id` is non-null.
- `job_applications.student_id` and `company_id` are non-null.
- Job deadline and `active` are checked by the application service before a student can apply.
- Student identity is derived from the authenticated JWT, not accepted from the request body.

## Operational notes

Keep `UPLOAD_DIRECTORY` private and persistent, back it up, and do not expose it as a static web directory. Database credentials and JWT signing keys must come from environment variables or a secret manager. No seed/demo users, companies, jobs or applications are inserted automatically.
