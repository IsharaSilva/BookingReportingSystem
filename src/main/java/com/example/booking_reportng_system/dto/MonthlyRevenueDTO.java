package com.example.booking_reportng_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRevenueDTO {
    private String month; // We will keep this as a String for your frontend JSON format
    private double revenue;

    // Change constructor parameters to accept Integer components from JPQL
    public MonthlyRevenueDTO(Integer year, Integer month, double revenue) {
        // Formats numbers cleanly into "YYYY-MM" format (e.g., 2026-07)
        this.month = String.format("%d-%02d", year, month);
        this.revenue = revenue;
    }
}