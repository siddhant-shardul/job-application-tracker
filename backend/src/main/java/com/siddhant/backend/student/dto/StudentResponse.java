package com.siddhant.backend.student.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudentResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String collegeName;
    private String degree;
    private String branch;
    private Integer graduationYear;
    private String currentLocation;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
