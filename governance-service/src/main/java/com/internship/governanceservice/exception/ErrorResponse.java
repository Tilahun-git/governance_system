package com.internship.governanceservice.exception;
public record ErrorResponse(

        int status,

        String error,

        String message
) {
}