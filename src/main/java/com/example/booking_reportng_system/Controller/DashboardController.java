package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.DashboardService;
import com.example.booking_reportng_system.dto.AgentBookingDTO;
import com.example.booking_reportng_system.dto.CountryRevenueDTO;
import com.example.booking_reportng_system.dto.DashboardSummaryDTO;
import com.example.booking_reportng_system.dto.MonthlyRevenueDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/revenue-country")
    public ResponseEntity<List<CountryRevenueDTO>> getRevenueByCountry() {
        return ResponseEntity.ok(dashboardService.getRevenueByCountry());
    }

    @GetMapping("/agent-bookings")
    public ResponseEntity<List<AgentBookingDTO>> getBookingsByAgent() {
        return ResponseEntity.ok(dashboardService.getBookingsByAgent());
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueDTO>> getMonthlyRevenueTrend() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenueTrend());
    }
}