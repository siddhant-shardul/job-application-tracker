package com.siddhant.backend.jobapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.siddhant.backend.jobapplication.entity.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}