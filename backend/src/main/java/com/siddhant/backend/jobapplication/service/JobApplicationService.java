package com.siddhant.backend.jobapplication.service;

import com.siddhant.backend.auth.entity.User;
import com.siddhant.backend.auth.repository.UserRepository;
import com.siddhant.backend.company.entity.Company;
import com.siddhant.backend.jobapplication.dto.JobApplicationRequest;
import com.siddhant.backend.job.entity.Job;
import com.siddhant.backend.job.repository.JobRepository;
import com.siddhant.backend.jobapplication.dto.JobApplicationResponse;
import com.siddhant.backend.jobapplication.entity.ApplicationStatus;
import com.siddhant.backend.jobapplication.entity.JobApplication;
import com.siddhant.backend.jobapplication.repository.JobApplicationRepository;
import com.siddhant.backend.student.entity.Student;
import com.siddhant.backend.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationService {
    private final JobApplicationRepository applications;
    private final StudentRepository students;
    private final UserRepository users;
    private final JobRepository jobs;

    public JobApplicationResponse createJobApplication(JobApplicationRequest request) {
        Student student = currentStudent();
        Job job;
        if (request.getJobId() != null) {
            job = jobs.findById(request.getJobId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job posting not found"));
        } else if (request.getCompanyId() != null && request.getJobRole() != null && !request.getJobRole().isBlank()) {
            job = jobs.findFirstByCompanyIdAndTitleIgnoreCaseAndActiveTrue(request.getCompanyId(), request.getJobRole().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply through an active job posting"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job posting is required");
        }
        if (!job.isActive() || (job.getDeadline() != null && job.getDeadline().isBefore(LocalDate.now()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This job posting is no longer accepting applications");
        }
        Company company = job.getCompany();
        String jobRole = job.getTitle();
        if (applications.existsByStudentIdAndCompanyIdAndJobRoleIgnoreCase(student.getId(), company.getId(), jobRole)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already applied for this role");
        }
        JobApplication application = JobApplication.builder()
                .student(student).company(company).jobRole(jobRole)
                .applicationDate(LocalDate.now()).status(ApplicationStatus.APPLIED)
                .notes(request.getNotes()).build();
        return map(applications.save(application));
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getJobApplications(Long studentId, Long companyId, ApplicationStatus status) {
        boolean admin = isAdmin();
        Student own = admin ? null : currentStudent();
        if (!admin && studentId != null && !studentId.equals(own.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot view another student's applications");
        }
        Long effectiveStudentId = admin ? studentId : own.getId();
        return applications.findWithFilters(effectiveStudentId, companyId, status)
                .stream().map(this::map).toList();
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse getJobApplicationById(Long id) {
        JobApplication app = get(id);
        verifyOwnerOrAdmin(app);
        return map(app);
    }

    public JobApplicationResponse updateJobApplication(Long id, JobApplicationRequest request) {
        if (!isAdmin()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only administrators can update application status");
        JobApplication app = get(id);
        if (request.getStatus() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
        app.setStatus(request.getStatus());
        if (request.getNotes() != null) app.setNotes(request.getNotes());
        return map(applications.save(app));
    }

    public void deleteJobApplication(Long id) {
        JobApplication app = get(id);
        verifyOwnerOrAdmin(app);
        if (!isAdmin() && app.getStatus() != ApplicationStatus.APPLIED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only applications in APPLIED status can be withdrawn");
        }
        applications.delete(app);
    }

    private Student currentStudent() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = users.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return students.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Create your student profile before applying"));
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    private void verifyOwnerOrAdmin(JobApplication app) {
        if (!isAdmin() && !app.getStudent().getUser().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    private JobApplication get(Long id) {
        return applications.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
    }
    private JobApplicationResponse map(JobApplication a) {
        return JobApplicationResponse.builder().id(a.getId()).studentId(a.getStudent().getId())
                .studentName(a.getStudent().getFullName()).companyId(a.getCompany().getId())
                .companyName(a.getCompany().getName()).jobRole(a.getJobRole())
                .applicationDate(a.getApplicationDate()).status(a.getStatus()).notes(a.getNotes())
                .createdAt(a.getCreatedAt()).updatedAt(a.getUpdatedAt()).build();
    }
}
