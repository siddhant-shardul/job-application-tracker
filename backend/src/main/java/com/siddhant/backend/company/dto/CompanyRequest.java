package com.siddhant.backend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    private String website;

    private String location;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
