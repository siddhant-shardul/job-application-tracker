package com.siddhant.backend.job.repository;

import com.siddhant.backend.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    @EntityGraph(attributePaths = "company")
    List<Job> findAllByOrderByCreatedAtDesc();
    @EntityGraph(attributePaths = "company")
    List<Job> findByActiveTrueOrderByCreatedAtDesc();
    @EntityGraph(attributePaths = "company")
    Optional<Job> findFirstByCompanyIdAndTitleIgnoreCaseAndActiveTrue(Long companyId, String title);
    @EntityGraph(attributePaths = "company")
    Optional<Job> findById(Long id);
    boolean existsByCompanyId(Long companyId);
}
