package com.siddhant.backend.jobapplication.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siddhant.backend.jobapplication.dto.JobApplicationRequest;
import com.siddhant.backend.jobapplication.dto.JobApplicationResponse;
import com.siddhant.backend.jobapplication.entity.ApplicationStatus;
import com.siddhant.backend.jobapplication.service.JobApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationResponse createJobApplication(
            @Valid @RequestBody JobApplicationRequest request
    ) {
        return jobApplicationService.createJobApplication(request);
    }

    @GetMapping
    public List<JobApplicationResponse> getJobApplications(
        @RequestParam(required = false) Long studentId,
        @RequestParam(required = false) Long companyId,
        @RequestParam(required = false) ApplicationStatus status
) {
    return jobApplicationService.getJobApplications(studentId, companyId, status);
}

    @GetMapping("/{id}")
    public JobApplicationResponse getJobApplicationById(@PathVariable Long id) {
        return jobApplicationService.getJobApplicationById(id);
    }

    @PutMapping("/{id}")
    public JobApplicationResponse updateJobApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        return jobApplicationService.updateJobApplication(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobApplication(@PathVariable Long id) {
        jobApplicationService.deleteJobApplication(id);
    }
}