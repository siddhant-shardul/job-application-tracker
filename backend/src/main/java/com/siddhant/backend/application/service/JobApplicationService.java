package com.siddhant.backend.application.service;

import com.siddhant.backend.application.dto.JobApplicationRequest;
import com.siddhant.backend.application.dto.JobApplicationResponse;
import com.siddhant.backend.application.entity.ApplicationStatus;
import com.siddhant.backend.application.entity.JobApplication;
import com.siddhant.backend.application.repository.JobApplicationRepository;
import com.siddhant.backend.company.entity.Company;
import com.siddhant.backend.company.repository.CompanyRepository;
import com.siddhant.backend.student.entity.Student;
import com.siddhant.backend.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    public JobApplicationResponse createApplication(JobApplicationRequest request) {
        Student student = findStudentById(request.getStudentId());
        Company company = findCompanyById(request.getCompanyId());

        if (jobApplicationRepository.existsByStudentIdAndCompanyIdAndJobRole(
                request.getStudentId(),
                request.getCompanyId(),
                request.getJobRole()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Application already exists for this student, company, and job role"
            );
        }

        JobApplication application = JobApplication.builder()
                .student(student)
                .company(company)
                .jobRole(request.getJobRole())
                .applicationDate(request.getApplicationDate())
                .status(request.getStatus() != null ? request.getStatus() : ApplicationStatus.APPLIED)
                .notes(request.getNotes())
                .build();

        JobApplication savedApplication = jobApplicationRepository.save(application);

        return mapToResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getAllApplications() {
        return jobApplicationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse getApplicationById(Long id) {
        JobApplication application = findApplicationById(id);
        return mapToResponse(application);
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getApplicationsByStudentId(Long studentId) {
        findStudentById(studentId);

        return jobApplicationRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getApplicationsByCompanyId(Long companyId) {
        findCompanyById(companyId);

        return jobApplicationRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobApplicationResponse updateApplication(Long id, JobApplicationRequest request) {
        JobApplication application = findApplicationById(id);

        Student student = findStudentById(request.getStudentId());
        Company company = findCompanyById(request.getCompanyId());

        boolean sameApplication =
                application.getStudent().getId().equals(request.getStudentId())
                        && application.getCompany().getId().equals(request.getCompanyId())
                        && application.getJobRole().equals(request.getJobRole());

        if (!sameApplication && jobApplicationRepository.existsByStudentIdAndCompanyIdAndJobRole(
                request.getStudentId(),
                request.getCompanyId(),
                request.getJobRole()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Application already exists for this student, company, and job role"
            );
        }

        application.setStudent(student);
        application.setCompany(company);
        application.setJobRole(request.getJobRole());
        application.setApplicationDate(request.getApplicationDate());
        application.setStatus(request.getStatus() != null ? request.getStatus() : ApplicationStatus.APPLIED);
        application.setNotes(request.getNotes());

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        return mapToResponse(updatedApplication);
    }

    public void deleteApplication(Long id) {
        JobApplication application = findApplicationById(id);
        jobApplicationRepository.delete(application);
    }

    private JobApplication findApplicationById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Job application not found with id: " + id
                ));
    }

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found with id: " + id
                ));
    }

    private Company findCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Company not found with id: " + id
                ));
    }

    private JobApplicationResponse mapToResponse(JobApplication application) {
        return JobApplicationResponse.builder()
                .id(application.getId())
                .studentId(application.getStudent().getId())
                .studentName(application.getStudent().getFullName())
                .companyId(application.getCompany().getId())
                .companyName(application.getCompany().getName())
                .jobRole(application.getJobRole())
                .applicationDate(application.getApplicationDate())
                .status(application.getStatus())
                .notes(application.getNotes())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
