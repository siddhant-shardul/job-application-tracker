package com.siddhant.backend.company.service;

import com.siddhant.backend.company.dto.CompanyRequest;
import com.siddhant.backend.company.entity.Company;
import com.siddhant.backend.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

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
        companyRepository.delete(company);
    }
}
