package com.siddhant.backend.jobapplication.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.siddhant.backend.jobapplication.entity.ApplicationStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobApplicationResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long companyId;
    private String companyName;

    private String jobRole;
    private LocalDate applicationDate;
    private ApplicationStatus status;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}