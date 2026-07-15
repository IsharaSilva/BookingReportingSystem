package com.example.booking_reportng_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryRevenueDTO {
    private String country;
    private double revenue;
}