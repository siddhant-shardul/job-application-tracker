package com.siddhant.backend.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;
}