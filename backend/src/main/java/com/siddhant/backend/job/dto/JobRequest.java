package com.siddhant.backend.job.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class JobRequest {
    @NotNull private Long companyId;
    @NotBlank @Size(max = 140) private String title;
    @Size(max = 4000) private String description;
    @Size(max = 120) private String location;
    @DecimalMin("0.0") private BigDecimal salaryLpa;
    @Size(max = 1200) private String eligibility;
    @Size(max = 1200) private String requiredSkills;
    private LocalDate deadline;
    private Boolean active;
}
