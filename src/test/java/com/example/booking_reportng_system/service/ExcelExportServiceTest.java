package com.example.booking_reportng_system.service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.Service.ExcelExportService;
import com.example.booking_reportng_system.models.Booking;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelExportServiceTest {

    private ExcelExportService excelExportService;

    @Mock
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        excelExportService = new ExcelExportService(bookingRepository);
    }

    @Test
    void testExportBookingsToExcel_Success() throws IOException {
        Booking booking1 = new Booking("BKG001", "Sarah Jenkins", "United States", "Adventure", LocalDate.of(2026, 7, 15), 1250.00, "Confirmed");
        Booking booking2 = new Booking("BKG002", "Michael Chang", "Germany", "Cultural", null, 890.00, "Cancelled");
        List<Booking> mockBookingsList = Arrays.asList(booking1, booking2);

        when(bookingRepository.findAll()).thenReturn(mockBookingsList);

        ByteArrayInputStream resultStream = excelExportService.exportBookingsToExcel();

        assertNotNull(resultStream, "The output array data must initialize safely.");

        try (Workbook workbook = new XSSFWorkbook(resultStream)) {
            Sheet sheet = workbook.getSheet("Bookings Raw Data");
            assertNotNull(sheet, "name mismatch found.");

            //Verify Grid Sizing Structure
            assertEquals(0, sheet.getFirstRowNum());
            assertEquals(2, sheet.getLastRowNum(), "Sheet must contain exactly 1 header block row and 2 data value row sets.");

            //Assert Headers Structural Alignment
            Row headerRow = sheet.getRow(0);
            assertNotNull(headerRow);
            assertEquals("Booking No", headerRow.getCell(0).getStringCellValue());
            assertEquals("Agent", headerRow.getCell(1).getStringCellValue());
            assertEquals("Amount", headerRow.getCell(5).getStringCellValue());
            assertEquals("Status", headerRow.getCell(6).getStringCellValue());

            //Assert Row 1 Entity Value Integrity
            Row dataRow1 = sheet.getRow(1);
            assertNotNull(dataRow1);
            assertEquals("BKG001", dataRow1.getCell(0).getStringCellValue());
            assertEquals(1250.00, dataRow1.getCell(5).getNumericCellValue());
            assertEquals("Confirmed", dataRow1.getCell(6).getStringCellValue());

            //Assert Row 2 Optional Null Date Handling Logic Bounds
            Row dataRow2 = sheet.getRow(2);
            assertNotNull(dataRow2);
            assertEquals("BKG002", dataRow2.getCell(0).getStringCellValue());
            assertEquals("", dataRow2.getCell(4).getStringCellValue(), "Null calendar must display as blank fields.");
            assertEquals("Cancelled", dataRow2.getCell(6).getStringCellValue());
        }
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testExportBookingsToExcel_EmptyDatabaseTable() throws IOException {
        // no database records are found
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        ByteArrayInputStream resultStream = excelExportService.exportBookingsToExcel();

        assertNotNull(resultStream);
        try (Workbook workbook = new XSSFWorkbook(resultStream)) {
            Sheet sheet = workbook.getSheet("Bookings Raw Data");
            assertNotNull(sheet, "The sheet 'Bookings Data' should exist even if the database is empty.");
            assertEquals(0, sheet.getLastRowNum(), "Only the header schema block row (index 0) must persist if the database is empty.");
        }
        verify(bookingRepository, times(1)).findAll();
    }
}