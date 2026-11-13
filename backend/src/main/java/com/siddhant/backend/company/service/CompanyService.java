package com.siddhant.backend.company.service;

import com.siddhant.backend.company.dto.CompanyRequest;
import com.siddhant.backend.company.entity.Company;
import com.siddhant.backend.company.repository.CompanyRepository;
import com.siddhant.backend.job.repository.JobRepository;
import com.siddhant.backend.jobapplication.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;

    public Company createCompany(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .website(request.getWebsite())
                .location(request.getLocation())
                .description(request.getDescription())
                .build();

        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Company not found with id: " + id
                ));
    }

    public Company updateCompany(Long id, CompanyRequest request) {
        Company company = getCompanyById(id);

        company.setName(request.getName());
        company.setWebsite(request.getWebsite());
        company.setLocation(request.getLocation());
        company.setDescription(request.getDescription());

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);
        if (jobRepository.existsByCompanyId(id) || applicationRepository.existsByCompanyId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company is linked to job postings or application history and cannot be deleted");
        }
        companyRepository.delete(company);
    }
}
