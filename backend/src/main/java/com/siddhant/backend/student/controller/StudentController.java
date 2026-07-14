package com.siddhant.backend.student.controller;

import com.siddhant.backend.student.dto.StudentRequest;
import com.siddhant.backend.student.dto.StudentResponse;
import com.siddhant.backend.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

private final StudentService studentService;

@PostMapping("/me")
@ResponseStatus(HttpStatus.CREATED)
public StudentResponse createMyProfile(
        @Valid @RequestBody StudentRequest request) {

    return studentService.createStudent(request);
}

@GetMapping("/me")
public StudentResponse getMyProfile() {
    return studentService.getMyProfile();
}

@PutMapping("/me")
public StudentResponse updateMyProfile(
        @Valid @RequestBody StudentRequest request) {

    return studentService.updateMyProfile(request);
}
}
