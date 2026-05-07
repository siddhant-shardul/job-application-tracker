package com.siddhant.backend.student.service;

import com.siddhant.backend.student.dto.StudentRequest;
import com.siddhant.backend.student.dto.StudentResponse;
import com.siddhant.backend.student.entity.Student;
import com.siddhant.backend.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponse createStudent(StudentRequest request) {
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
                .currentLocation(request.getCurrentLocation())
                .linkedinUrl(request.getLinkedinUrl())
                .githubUrl(request.getGithubUrl())
                .portfolioUrl(request.getPortfolioUrl())
                .build();

        Student savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public StudentResponse getStudentById(Long id) {
        Student student = findStudentById(id);
        return mapToResponse(student);
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = findStudentById(id);

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
        student.setCurrentLocation(request.getCurrentLocation());
        student.setLinkedinUrl(request.getLinkedinUrl());
        student.setGithubUrl(request.getGithubUrl());
        student.setPortfolioUrl(request.getPortfolioUrl());

        Student updatedStudent = studentRepository.save(student);

        return mapToResponse(updatedStudent);
    }

    public void deleteStudent(Long id) {
        Student student = findStudentById(id);
        studentRepository.delete(student);
    }

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found with id: " + id
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
                .currentLocation(student.getCurrentLocation())
                .linkedinUrl(student.getLinkedinUrl())
                .githubUrl(student.getGithubUrl())
                .portfolioUrl(student.getPortfolioUrl())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
