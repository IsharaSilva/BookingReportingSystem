package com.example.booking_reportng_system.service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.Service.DashboardService;
import com.example.booking_reportng_system.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    private DashboardService dashboardService;

    @Mock
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dashboardService = new DashboardService(bookingRepository);
    }

    @Test
    void testGetSummary_Success() {
        //the main KPI cards
        DashboardSummaryDTO mockSummary = new DashboardSummaryDTO(1024L, 842L, 182L, 148250.00);
        when(bookingRepository.getDashboardSummary()).thenReturn(mockSummary);

        DashboardSummaryDTO result = dashboardService.getSummary();

        assertNotNull(result);
        assertEquals(1024L, result.getTotalBookings());
        assertEquals(842L, result.getConfirmed());
        assertEquals(182L, result.getCancelled());
        assertEquals(148250.00, result.getTotalRevenue());
        verify(bookingRepository, times(1)).getDashboardSummary();
    }

    @Test
    void testGetRevenueByCountry_Success() {
        //Revenue by Country Bar Chart
        List<CountryRevenueDTO> mockCountryRevenue = List.of(
                new CountryRevenueDTO("Sri Lanka", 95000.00),
                new CountryRevenueDTO("Maldives", 53250.00)
        );
        when(bookingRepository.getRevenueByCountry()).thenReturn(mockCountryRevenue);

        List<CountryRevenueDTO> result = dashboardService.getRevenueByCountry();

        assertNotNull(result);
        assertEquals(2, result.size(), "Should return exactly 2 country records");
        assertEquals("Sri Lanka", result.get(0).getCountry());
        assertEquals(95000.00, result.get(0).getRevenue());
        assertEquals("Maldives", result.get(1).getCountry());
        assertEquals(53250.00, result.get(1).getRevenue());
        verify(bookingRepository, times(1)).getRevenueByCountry();
    }

    @Test
    void testGetBookingsByAgent_Success() {
        //Top 5 Performing Agents Horizontal Bar Chart
        List<AgentBookingDTO> mockAgentBookings = List.of(
                new AgentBookingDTO("Agent Alpha", 150L),
                new AgentBookingDTO("Agent Beta", 120L)
        );
        when(bookingRepository.getBookingsByAgent()).thenReturn(mockAgentBookings);

        List<AgentBookingDTO> result = dashboardService.getBookingsByAgent();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Agent Alpha", result.get(0).getAgent());
        assertEquals(150L, result.get(0).getBookingsCount());
        verify(bookingRepository, times(1)).getBookingsByAgent();
    }

    @Test
    void testGetMonthlyRevenueTrend_Success() {
        //Monthly Revenue Trend Line Chart
        List<MonthlyRevenueDTO> mockMonthlyRevenue = List.of(
                new MonthlyRevenueDTO("2026-06", 60000.00),
                new MonthlyRevenueDTO("2026-07", 88250.00)
        );
        when(bookingRepository.getMonthlyRevenueTrend()).thenReturn(mockMonthlyRevenue);

        List<MonthlyRevenueDTO> result = dashboardService.getMonthlyRevenueTrend();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2026-06", result.get(0).getMonth());
        assertEquals(60000.00, result.get(0).getRevenue());
        verify(bookingRepository, times(1)).getMonthlyRevenueTrend();
    }
}