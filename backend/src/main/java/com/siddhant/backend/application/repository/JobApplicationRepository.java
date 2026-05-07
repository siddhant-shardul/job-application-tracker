package com.siddhant.backend.application.repository;

import com.siddhant.backend.application.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStudentId(Long studentId);

    List<JobApplication> findByCompanyId(Long companyId);

    boolean existsByStudentIdAndCompanyIdAndJobRole(Long studentId, Long companyId, String jobRole);
}
