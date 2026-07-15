package com.example.booking_reportng_system.Service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final BookingRepository bookingRepository;

    public DashboardService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public DashboardSummaryDTO getSummary() {
        return bookingRepository.getDashboardSummary();
    }

    public List<CountryRevenueDTO> getRevenueByCountry() {
        return bookingRepository.getRevenueByCountry();
    }

    public List<AgentBookingDTO> getBookingsByAgent() {
        return bookingRepository.getBookingsByAgent();
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenueTrend() {
        return bookingRepository.getMonthlyRevenueTrend();
    }
}