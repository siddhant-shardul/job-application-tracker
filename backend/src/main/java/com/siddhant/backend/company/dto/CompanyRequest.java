package com.siddhant.backend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompanyRequest {
    @NotBlank(message = "Company name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 255)
    @Pattern(regexp = "(?i)^$|^https?://\\S+$", message = "Website must begin with http:// or https://")
    private String website;

    @Size(max = 120)
    private String location;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
