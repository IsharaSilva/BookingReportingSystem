package com.example.booking_reportng_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRevenueDTO {
    private String month;
    private double revenue;

    public MonthlyRevenueDTO(Integer year, Integer month, double revenue) {
        // Formats numbers cleanly into "YYYY-MM" format
        this.month = String.format("%d-%02d", year, month);
        this.revenue = revenue;
    }
}