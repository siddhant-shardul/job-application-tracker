package com.siddhant.backend.job.service;

import com.siddhant.backend.company.entity.Company;
import com.siddhant.backend.company.repository.CompanyRepository;
import com.siddhant.backend.job.dto.JobRequest;
import com.siddhant.backend.job.dto.JobResponse;
import com.siddhant.backend.job.entity.Job;
import com.siddhant.backend.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service @RequiredArgsConstructor @Transactional
public class JobService {
    private final JobRepository jobs;
    private final CompanyRepository companies;

    @Transactional(readOnly = true)
    public List<JobResponse> list(String search, boolean includeInactive) {
        var all = includeInactive ? jobs.findAllByOrderByCreatedAtDesc() : jobs.findByActiveTrueOrderByCreatedAtDesc();
        String term = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        return all.stream().filter(j -> term.isBlank() || contains(j.getTitle(), term) || contains(j.getCompany().getName(), term) || contains(j.getLocation(), term) || contains(j.getRequiredSkills(), term))
                .filter(j -> includeInactive || j.getDeadline() == null || !j.getDeadline().isBefore(LocalDate.now()))
                .map(this::map).toList();
    }
    @Transactional(readOnly = true)
    public JobResponse get(Long id) { return map(find(id)); }
    public JobResponse create(JobRequest request) { Job j = new Job(); apply(j, request); return map(jobs.save(j)); }
    public JobResponse update(Long id, JobRequest request) { Job j = find(id); apply(j, request); return map(jobs.save(j)); }
    public void delete(Long id) { jobs.delete(find(id)); }
    private Job find(Long id) { return jobs.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found")); }
    private void apply(Job j, JobRequest r) {
        Company company = companies.findById(r.getCompanyId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        j.setCompany(company); j.setTitle(r.getTitle().trim()); j.setDescription(r.getDescription()); j.setLocation(r.getLocation());
        j.setSalaryLpa(r.getSalaryLpa()); j.setEligibility(r.getEligibility()); j.setRequiredSkills(r.getRequiredSkills()); j.setDeadline(r.getDeadline());
        if (r.getActive() != null) j.setActive(r.getActive());
    }
    private boolean contains(String value, String term) { return value != null && value.toLowerCase(Locale.ROOT).contains(term); }
    private JobResponse map(Job j) {
        boolean expired = j.getDeadline() != null && j.getDeadline().isBefore(LocalDate.now());
        return JobResponse.builder().id(j.getId()).companyId(j.getCompany().getId()).companyName(j.getCompany().getName()).title(j.getTitle())
                .description(j.getDescription()).location(j.getLocation()).salaryLpa(j.getSalaryLpa()).eligibility(j.getEligibility())
                .requiredSkills(j.getRequiredSkills()).deadline(j.getDeadline()).active(j.isActive()).expired(expired).createdAt(j.getCreatedAt()).build();
    }
}
