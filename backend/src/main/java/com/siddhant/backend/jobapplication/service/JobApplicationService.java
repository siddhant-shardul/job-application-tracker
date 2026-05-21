package com.siddhant.backend.jobapplication.service;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.siddhant.backend.company.entity.Company;
import com.siddhant.backend.company.repository.CompanyRepository;
import com.siddhant.backend.jobapplication.dto.JobApplicationRequest;
import com.siddhant.backend.jobapplication.dto.JobApplicationResponse;
import com.siddhant.backend.jobapplication.entity.ApplicationStatus;
import com.siddhant.backend.jobapplication.entity.JobApplication;
import com.siddhant.backend.jobapplication.repository.JobApplicationRepository;
import com.siddhant.backend.student.entity.Student;
import com.siddhant.backend.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    public JobApplicationResponse createJobApplication(JobApplicationRequest request) {
        Student student = getStudentOrThrow(request.getStudentId());
        Company company = getCompanyOrThrow(request.getCompanyId());

        JobApplication jobApplication = JobApplication.builder()
                .student(student)
                .company(company)
                .jobRole(request.getJobRole())
                .applicationDate(request.getApplicationDate())
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        return mapToResponse(savedJobApplication);
    }

    public List<JobApplicationResponse> getJobApplications(
        Long studentId,
        Long companyId,
        ApplicationStatus status
) {
    return jobApplicationRepository.findWithFilters(studentId, companyId, status)
            .stream()
            .map(this::mapToResponse)
            .toList();
}

    public JobApplicationResponse getJobApplicationById(Long id) {
        JobApplication jobApplication = getJobApplicationOrThrow(id);
        return mapToResponse(jobApplication);
    }

    public JobApplicationResponse updateJobApplication(Long id, JobApplicationRequest request) {
        JobApplication jobApplication = getJobApplicationOrThrow(id);

        Student student = getStudentOrThrow(request.getStudentId());
        Company company = getCompanyOrThrow(request.getCompanyId());

        jobApplication.setStudent(student);
        jobApplication.setCompany(company);
        jobApplication.setJobRole(request.getJobRole());
        jobApplication.setNotes(request.getNotes());

        if (request.getApplicationDate() != null) {
            jobApplication.setApplicationDate(request.getApplicationDate());
        }

        if (request.getStatus() != null) {
            jobApplication.setStatus(request.getStatus());
        }

        JobApplication updatedJobApplication = jobApplicationRepository.save(jobApplication);

        return mapToResponse(updatedJobApplication);
    }

    public void deleteJobApplication(Long id) {
        JobApplication jobApplication = getJobApplicationOrThrow(id);
        jobApplicationRepository.delete(jobApplication);
    }

    private JobApplication getJobApplicationOrThrow(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Job application not found with id: " + id
                ));
    }

    private Student getStudentOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found with id: " + studentId
                ));
    }

    private Company getCompanyOrThrow(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Company not found with id: " + companyId
                ));
    }

    private JobApplicationResponse mapToResponse(JobApplication jobApplication) {
        return JobApplicationResponse.builder()
                .id(jobApplication.getId())
                .studentId(jobApplication.getStudent().getId())
                .studentName(jobApplication.getStudent().getFullName())
                .companyId(jobApplication.getCompany().getId())
                .companyName(jobApplication.getCompany().getName())
                .jobRole(jobApplication.getJobRole())
                .applicationDate(jobApplication.getApplicationDate())
                .status(jobApplication.getStatus())
                .notes(jobApplication.getNotes())
                .createdAt(jobApplication.getCreatedAt())
                .updatedAt(jobApplication.getUpdatedAt())
                .build();
    }
}