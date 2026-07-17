package com.example.booking_reportng_system.service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.Service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingService = new BookingService(bookingRepository);
    }

    @Test
    void testImportCsv_AllValidData() {
        String csvContent = """
                Booking No,Agent,Country,Tour Type,Booking Date,Amount,Status
                BKG100,Agent Alpha,Sri Lanka,Adventure,2026-07-15,1200.00,Confirmed
                BKG101,Agent Beta,Maldives,Leisure,2026-07-16,850.50,Pending
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file", "bookings.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8)
        );

        when(bookingRepository.existsByBookingNo(anyString())).thenReturn(false);

        String result = bookingService.importCsv(file);

        assertTrue(result.contains("Successfully imported: 2 rows"), "Should process all valid rows successfully");
        assertTrue(result.contains("Skipped: 0 invalid rows"));
        verify(bookingRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    void testImportCsv_NegativeAmountAndInvalidFormat() {
        String csvContent = """
                Booking No,Agent,Country,Tour Type,Booking Date,Amount,Status
                BKG200,Expedia,Sri Lanka,Adventure,2026-07-15,1200.00,Confirmed
                BKG201,TUI,Maldives,Leisure,2026-07-16,-50.00,Cancelled
                BKG202, Agoda,India,Culture,2026-07-17,FREE_PROMO,Confirmed
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file", "bookings.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8)
        );

        when(bookingRepository.existsByBookingNo(anyString())).thenReturn(false);

        String result = bookingService.importCsv(file);

        assertTrue(result.contains("Successfully imported: 1 rows"), "Only the positive/valid amount row should save");
        assertTrue(result.contains("Skipped: 2 invalid rows"), "Negative and string amounts should be filtered out");
        verify(bookingRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    void testImportCsv_MissingMandatoryFields() {
        String csvContent = """
                Booking No,Agent,Country,Tour Type,Booking Date,Amount,Status
                BKG300,Expedia,Sri Lanka,Adventure,2026-07-15,1200.00,Confirmed
                BKG301,,Sri Lanka,Adventure,2026-07-15,400.00,Confirmed
                BKG302,Agent Gamma,India,Culture,,500.00,Confirmed
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file", "bookings.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8)
        );

        when(bookingRepository.existsByBookingNo(anyString())).thenReturn(false);

        String result = bookingService.importCsv(file);

        assertTrue(result.contains("Successfully imported: 1 rows"));
        assertTrue(result.contains("Skipped: 2 invalid rows"), "Missing mandatory fields must be skipped");
    }

    @Test
    void testImportCsv_DuplicateBookingNumbers() {
        String csvContent = """
                Booking No,Agent,Country,Tour Type,Booking Date,Amount,Status
                BKG400,Expedia,Sri Lanka,Adventure,2026-07-15,1200.00,Confirmed
                BKG400,TUI,Sri Lanka,Adventure,2026-07-15,1200.00,Confirmed
                BKG_DB_EXIST,Agoda,UK,Business,2026-07-16,300.00,Confirmed
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file", "bookings.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8)
        );

        when(bookingRepository.existsByBookingNo("BKG400")).thenReturn(false, false);
        when(bookingRepository.existsByBookingNo("BKG_DB_EXIST")).thenReturn(true);

        String result = bookingService.importCsv(file);

        assertTrue(result.contains("Successfully imported: 1 rows"));
        assertTrue(result.contains("Skipped: 2 invalid rows"), "In-file duplicate and in-DB duplicate should be rejected");
    }

    @Test
    void testImportCsv_InvalidDateFormat() {
        String csvContent = """
                Booking No,Agent,Country,Tour Type,Booking Date,Amount,Status
                BKG500,Expedia,Sri Lanka,Adventure,2026-07-15,1200.00,Confirmed
                BKG501,TUI,Maldives,Leisure,15/07/2026,600.00,Pending
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file", "bookings.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8)
        );

        when(bookingRepository.existsByBookingNo(anyString())).thenReturn(false);

        String result = bookingService.importCsv(file);

        assertTrue(result.contains("Successfully imported: 1 rows"));
        assertTrue(result.contains("Skipped: 1 invalid rows"), "Dates not using YYYY-MM-DD pattern must be skipped");
    }
}