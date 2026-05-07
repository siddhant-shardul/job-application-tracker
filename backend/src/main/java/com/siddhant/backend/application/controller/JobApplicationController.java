package com.siddhant.backend.application.controller;

import com.siddhant.backend.application.dto.JobApplicationRequest;
import com.siddhant.backend.application.dto.JobApplicationResponse;
import com.siddhant.backend.application.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationResponse createApplication(@Valid @RequestBody JobApplicationRequest request) {
        return jobApplicationService.createApplication(request);
    }

    @GetMapping
    public List<JobApplicationResponse> getAllApplications() {
        return jobApplicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public JobApplicationResponse getApplicationById(@PathVariable Long id) {
        return jobApplicationService.getApplicationById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<JobApplicationResponse> getApplicationsByStudentId(@PathVariable Long studentId) {
        return jobApplicationService.getApplicationsByStudentId(studentId);
    }

    @GetMapping("/company/{companyId}")
    public List<JobApplicationResponse> getApplicationsByCompanyId(@PathVariable Long companyId) {
        return jobApplicationService.getApplicationsByCompanyId(companyId);
    }

    @PutMapping("/{id}")
    public JobApplicationResponse updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        return jobApplicationService.updateApplication(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplication(@PathVariable Long id) {
        jobApplicationService.deleteApplication(id);
    }
}
