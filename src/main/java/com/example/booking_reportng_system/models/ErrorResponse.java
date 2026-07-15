package com.example.booking_reportng_system.models;

public record ErrorResponse(
        String status,
        String message,
        long timestamp
) {}
