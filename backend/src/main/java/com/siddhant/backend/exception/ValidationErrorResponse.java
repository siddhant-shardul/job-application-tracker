package com.siddhant.backend.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationErrorResponse {

    private int status;

    private String message;

    private LocalDateTime timestamp;

    private Map<String, String> errors;
}