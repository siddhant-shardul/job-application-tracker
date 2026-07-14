package com.siddhant.backend.student.repository;

import com.siddhant.backend.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    Optional<Student> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}