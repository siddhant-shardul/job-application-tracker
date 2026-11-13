package com.siddhant.backend.job.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Builder
public class JobResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private String title;
    private String description;
    private String location;
    private BigDecimal salaryLpa;
    private String eligibility;
    private String requiredSkills;
    private LocalDate deadline;
    private boolean active;
    private boolean expired;
    private LocalDateTime createdAt;
}
