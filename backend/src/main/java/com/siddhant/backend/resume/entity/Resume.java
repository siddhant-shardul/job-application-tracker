package com.siddhant.backend.resume.entity;

import com.siddhant.backend.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "resumes", uniqueConstraints = @UniqueConstraint(name = "uk_resume_student", columnNames = "student_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resume {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "student_id", nullable = false, unique = true) private Student student;
    @Column(nullable = false) private String storedName;
    @Column(nullable = false) private String originalName;
    @Column(nullable = false) private String contentType;
    @Column(name = "file_size", nullable = false) private long size;
    private LocalDateTime uploadedAt;
    @PrePersist void onCreate() { uploadedAt = LocalDateTime.now(); }
}
