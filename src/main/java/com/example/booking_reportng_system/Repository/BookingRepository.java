package com.example.booking_reportng_system.Repository;

import com.example.booking_reportng_system.models.Booking;
import com.example.booking_reportng_system.dto.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    boolean existsByBookingNo(String bookingNo);

    @Query("SELECT new com.example.booking_reportng_system.dto.DashboardSummaryDTO(" +
            "COUNT(b), " +
            "SUM(CASE WHEN b.status = 'Confirmed' THEN 1L ELSE 0L END), " +
            "SUM(CASE WHEN b.status = 'Cancelled' THEN 1L ELSE 0L END), " +
            "SUM(CASE WHEN b.status = 'Pending' THEN 1L ELSE 0L END), " +
            "COALESCE(SUM(b.amount), 0.0)) " +
            "FROM Booking b")
    DashboardSummaryDTO getDashboardSummary();

    @Query("SELECT new com.example.booking_reportng_system.dto.CountryRevenueDTO(b.country, SUM(b.amount)) " +
            "FROM Booking b " +
            "GROUP BY b.country " +
            "ORDER BY SUM(b.amount) DESC")
    List<CountryRevenueDTO> getRevenueByCountry();

    @Query("SELECT new com.example.booking_reportng_system.dto.AgentBookingDTO(b.agent, COUNT(b)) " +
            "FROM Booking b " +
            "GROUP BY b.agent " +
            "ORDER BY COUNT(b) DESC")
    List<AgentBookingDTO> getBookingsByAgent();

    @Query("SELECT new com.example.booking_reportng_system.dto.MonthlyRevenueDTO(" +
            "YEAR(b.bookingDate), MONTH(b.bookingDate), SUM(b.amount)) " +
            "FROM Booking b " +
            "GROUP BY YEAR(b.bookingDate), MONTH(b.bookingDate) " +
            "ORDER BY YEAR(b.bookingDate) ASC, MONTH(b.bookingDate) ASC")
    List<MonthlyRevenueDTO> getMonthlyRevenueTrend();
}