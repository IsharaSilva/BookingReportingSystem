package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.DashboardService;
import com.example.booking_reportng_system.dto.AgentBookingDTO;
import com.example.booking_reportng_system.dto.CountryRevenueDTO;
import com.example.booking_reportng_system.dto.DashboardSummaryDTO;
import com.example.booking_reportng_system.dto.MonthlyRevenueDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard Analytics", description = "Endpoints for fetching travel metric reports and chart summaries")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get Booking and Revenue Metrics Summary", description = "Returns total metrics across confirmed and cancelled reservations.")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/revenue-country")
    @Operation(summary = "Get Revenue broken down by Country", description = "Returns aggregate financial contributions categorized by country origination.")
    public ResponseEntity<List<CountryRevenueDTO>> getRevenueByCountry() {
        return ResponseEntity.ok(dashboardService.getRevenueByCountry());
    }

    @GetMapping("/agent-bookings")
    @Operation(summary = "Get booking tallies per agent", description = "Returns distribution metrics breakdown for each individual operating agent.")
    public ResponseEntity<List<AgentBookingDTO>> getBookingsByAgent() {
        return ResponseEntity.ok(dashboardService.getBookingsByAgent());
    }

    @GetMapping("/monthly-revenue")
    @Operation(summary = "Get monthly financial progression trends", description = "Returns time-series data organizing transaction streams separated into calendar months.")
    public ResponseEntity<List<MonthlyRevenueDTO>> getMonthlyRevenueTrend() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenueTrend());
    }
}