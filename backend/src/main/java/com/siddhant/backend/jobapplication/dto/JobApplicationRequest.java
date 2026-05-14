package com.siddhant.backend.jobapplication.dto;

import java.time.LocalDate;

import com.siddhant.backend.jobapplication.entity.ApplicationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotBlank(message = "Job role is required")
    private String jobRole;

    private LocalDate applicationDate;

    private ApplicationStatus status;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}