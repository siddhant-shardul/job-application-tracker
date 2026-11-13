package com.siddhant.backend.job.entity;

import com.siddhant.backend.company.entity.Company;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs", indexes = {@Index(name = "idx_jobs_active_deadline", columnList = "active, deadline"), @Index(name = "idx_jobs_company", columnList = "company_id")})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "company_id", nullable = false) private Company company;
    @Column(nullable = false, length = 140) private String title;
    @Column(length = 4000) private String description;
    @Column(length = 120) private String location;
    @Column(precision = 12, scale = 2) private BigDecimal salaryLpa;
    @Column(length = 1200) private String eligibility;
    @Column(length = 1200) private String requiredSkills;
    private LocalDate deadline;
    @Builder.Default @Column(nullable = false) private boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
}
