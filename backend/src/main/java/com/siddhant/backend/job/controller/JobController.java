package com.siddhant.backend.job.controller;

import com.siddhant.backend.job.dto.JobRequest;
import com.siddhant.backend.job.dto.JobResponse;
import com.siddhant.backend.job.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/jobs") @RequiredArgsConstructor
public class JobController {
    private final JobService service;
    @GetMapping public List<JobResponse> list(@RequestParam(required = false) String search, @RequestParam(defaultValue = "false") boolean includeInactive) { return service.list(search, includeInactive); }
    @GetMapping("/{id}") public JobResponse get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public JobResponse create(@Valid @RequestBody JobRequest request) { return service.create(request); }
    @PutMapping("/{id}") public JobResponse update(@PathVariable Long id, @Valid @RequestBody JobRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
