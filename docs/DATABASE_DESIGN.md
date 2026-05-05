\# Database Design



\## Project



Student Placement / Job Application Tracker



\## Initial Tables



\### companies



Stores company details.



| Column | Type | Purpose |

|---|---|---|

| id | BIGINT | Primary key |

| name | VARCHAR | Company name |

| location | VARCHAR | Company location |

| website | VARCHAR | Company website |

| contact\_email | VARCHAR | HR/contact email |

| created\_at | TIMESTAMP | Record creation time |

| updated\_at | TIMESTAMP | Last update time |



\## Future Tables



\### students



Stores student profile data.



\### job\_applications



Stores applications submitted by students to companies.



\### skills



Stores student skills.



\### resumes



Stores uploaded resume metadata.



\## Relationships



One student can have many job applications.



One company can have many job applications.



The job\_applications table will connect students and companies.



\## First API



The first CRUD API will be for companies because it is simple and does not require authentication.

