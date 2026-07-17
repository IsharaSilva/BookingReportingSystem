package com.example.booking_reportng_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "booking_no", unique = true, nullable = false)
    @NotBlank(message = "Booking number is mandatory")
    private String bookingNo;

    @NotBlank(message = "Agent is mandatory")
    private String agent;

    @NotBlank(message = "Country is mandatory")
    private String country;

    @Column(name = "tour_type")
    private String tourType;

    @Column(name = "booking_date")
    @NotNull(message = "Booking Date is mandatory")
    private LocalDate bookingDate;

    @NotNull(message = "Amount is mandatory")
    @Min(value = 0, message = "Amount cannot be negative")
    private Double amount;

    private String status;
}
