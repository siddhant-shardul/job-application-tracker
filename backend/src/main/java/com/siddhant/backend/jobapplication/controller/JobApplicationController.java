package com.siddhant.backend.jobapplication.controller;

import com.siddhant.backend.jobapplication.dto.JobApplicationRequest;
import com.siddhant.backend.jobapplication.dto.JobApplicationResponse;
import com.siddhant.backend.jobapplication.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<JobApplicationResponse> getAllJobApplications() {
        return jobApplicationService.getAllJobApplications();
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