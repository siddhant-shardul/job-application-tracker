package com.siddhant.backend.student.service;

import com.siddhant.backend.auth.entity.User;
import com.siddhant.backend.auth.repository.UserRepository;
import com.siddhant.backend.student.dto.StudentRequest;
import com.siddhant.backend.student.dto.StudentResponse;
import com.siddhant.backend.student.entity.Student;
import com.siddhant.backend.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

private final StudentRepository studentRepository;
private final UserRepository userRepository;

public StudentResponse createStudent(StudentRequest request) {

    User user = getCurrentUser();

    if (studentRepository.existsByUserId(user.getId())) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Student profile already exists for this user"
        );
    }

    if (studentRepository.existsByEmail(request.getEmail())) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Student already exists with email: " + request.getEmail()
        );
    }

    Student student = Student.builder()
            .fullName(request.getFullName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .collegeName(request.getCollegeName())
            .degree(request.getDegree())
            .branch(request.getBranch())
            .graduationYear(request.getGraduationYear())
            .academicYear(request.getAcademicYear())
            .cgpa(request.getCgpa())
            .skills(request.getSkills())
            .currentLocation(request.getCurrentLocation())
            .linkedinUrl(request.getLinkedinUrl())
            .githubUrl(request.getGithubUrl())
            .portfolioUrl(request.getPortfolioUrl())
            .user(user)
            .build();

    return mapToResponse(studentRepository.save(student));
}

public StudentResponse getMyProfile() {

    User user = getCurrentUser();

    Student student = studentRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student profile not found"
            ));

    return mapToResponse(student);
}

public StudentResponse updateMyProfile(StudentRequest request) {

    User user = getCurrentUser();

    Student student = studentRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student profile not found"
            ));

    if (!student.getEmail().equals(request.getEmail())
            && studentRepository.existsByEmail(request.getEmail())) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Student already exists with email: " + request.getEmail()
        );
    }

    student.setFullName(request.getFullName());
    student.setEmail(request.getEmail());
    student.setPhone(request.getPhone());
    student.setCollegeName(request.getCollegeName());
    student.setDegree(request.getDegree());
    student.setBranch(request.getBranch());
    student.setGraduationYear(request.getGraduationYear());
    student.setAcademicYear(request.getAcademicYear());
    student.setCgpa(request.getCgpa());
    student.setSkills(request.getSkills());
    student.setCurrentLocation(request.getCurrentLocation());
    student.setLinkedinUrl(request.getLinkedinUrl());
    student.setGithubUrl(request.getGithubUrl());
    student.setPortfolioUrl(request.getPortfolioUrl());

    return mapToResponse(studentRepository.save(student));
}

public List<StudentResponse> getAllStudents() {
    return studentRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
}

private User getCurrentUser() {

    String email = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            ));
}

private StudentResponse mapToResponse(Student student) {
    return StudentResponse.builder()
            .id(student.getId())
            .fullName(student.getFullName())
            .email(student.getEmail())
            .phone(student.getPhone())
            .collegeName(student.getCollegeName())
            .degree(student.getDegree())
            .branch(student.getBranch())
            .graduationYear(student.getGraduationYear())
            .academicYear(student.getAcademicYear())
            .cgpa(student.getCgpa())
            .skills(student.getSkills())
            .currentLocation(student.getCurrentLocation())
            .linkedinUrl(student.getLinkedinUrl())
            .githubUrl(student.getGithubUrl())
            .portfolioUrl(student.getPortfolioUrl())
            .createdAt(student.getCreatedAt())
            .updatedAt(student.getUpdatedAt())
            .build();
}
}
