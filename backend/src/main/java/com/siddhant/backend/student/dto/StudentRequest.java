package com.siddhant.backend.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private Integer graduationYear;

    private String currentLocation;

    private String linkedinUrl;

    private String githubUrl;

    private String portfolioUrl;
}
