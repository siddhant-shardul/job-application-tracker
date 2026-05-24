package com.siddhant.backend.dashboard.service;

import com.siddhant.backend.company.repository.CompanyRepository;
import com.siddhant.backend.dashboard.dto.DashboardResponse;
import com.siddhant.backend.jobapplication.entity.ApplicationStatus;
import com.siddhant.backend.jobapplication.repository.JobApplicationRepository;
import com.siddhant.backend.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;

    private final CompanyRepository companyRepository;

    private final JobApplicationRepository jobApplicationRepository;

    public DashboardResponse getDashboardStats() {

        long totalStudents = studentRepository.count();

        long totalCompanies = companyRepository.count();

        long totalApplications = jobApplicationRepository.count();

        long appliedCount =
                jobApplicationRepository.countByStatus(ApplicationStatus.APPLIED);

        long shortlistedCount =
                jobApplicationRepository.countByStatus(ApplicationStatus.SHORTLISTED);

        long interviewCount =
                jobApplicationRepository.countByStatus(ApplicationStatus.INTERVIEW);

        long selectedCount =
                jobApplicationRepository.countByStatus(ApplicationStatus.SELECTED);

        long rejectedCount =
                jobApplicationRepository.countByStatus(ApplicationStatus.REJECTED);

        return DashboardResponse.builder()
                .totalStudents(totalStudents)
                .totalCompanies(totalCompanies)
                .totalApplications(totalApplications)
                .appliedCount(appliedCount)
                .shortlistedCount(shortlistedCount)
                .interviewCount(interviewCount)
                .selectedCount(selectedCount)
                .rejectedCount(rejectedCount)
                .build();
    }
}