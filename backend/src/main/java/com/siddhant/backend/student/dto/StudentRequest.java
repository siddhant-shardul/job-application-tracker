package com.siddhant.backend.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phone;

    private String collegeName;

    private String degree;

    private String branch;

    @Min(2000) @Max(2100)
    private Integer graduationYear;

    @Size(max = 40)
    private String academicYear;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double cgpa;

    @Size(max = 1000)
    private String skills;

    private String currentLocation;

    private String linkedinUrl;

    private String githubUrl;

    private String portfolioUrl;
}
