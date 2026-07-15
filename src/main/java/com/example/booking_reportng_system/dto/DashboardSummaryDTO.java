package com.example.booking_reportng_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {
    private long totalBookings;
    private long confirmed;
    private long cancelled;
    private double totalRevenue;
}