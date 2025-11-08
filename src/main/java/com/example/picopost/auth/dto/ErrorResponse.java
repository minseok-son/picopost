package com.example.picopost.auth.dto;

import java.time.Instant;

public class ErrorResponse {

    private final int statusCode;
    private final String message; 
    private final Instant timestamp;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = Instant.now();
    }

}
