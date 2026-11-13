package com.siddhant.backend.jobapplication.dto;

import java.time.LocalDate;

import com.siddhant.backend.jobapplication.entity.ApplicationStatus;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationRequest {

    // Student identity is derived from the JWT; any client-supplied ID is ignored.
    private Long studentId;

    // A current job-posting ID is used for new applications. Legacy company/role fields remain for compatibility.
    private Long jobId;

    private Long companyId;

    private String jobRole;

    private LocalDate applicationDate;

    private ApplicationStatus status;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}