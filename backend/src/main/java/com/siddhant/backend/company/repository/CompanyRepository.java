package com.siddhant.backend.company.repository;

import com.siddhant.backend.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
