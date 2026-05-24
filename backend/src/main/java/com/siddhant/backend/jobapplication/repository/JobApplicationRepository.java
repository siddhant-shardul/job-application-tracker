package com.siddhant.backend.jobapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.siddhant.backend.jobapplication.entity.ApplicationStatus;
import com.siddhant.backend.jobapplication.entity.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    @Query("""
            SELECT ja FROM JobApplication ja
            WHERE (:studentId IS NULL OR ja.student.id = :studentId)
              AND (:companyId IS NULL OR ja.company.id = :companyId)
              AND (:status IS NULL OR ja.status = :status)
            """)
    List<JobApplication> findWithFilters(
            @Param("studentId") Long studentId,
            @Param("companyId") Long companyId,
            @Param("status") ApplicationStatus status
    );

    long countByStatus(ApplicationStatus status);
}