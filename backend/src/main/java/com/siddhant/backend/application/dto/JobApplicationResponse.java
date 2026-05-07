package com.siddhant.backend.application.dto;

import com.siddhant.backend.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
