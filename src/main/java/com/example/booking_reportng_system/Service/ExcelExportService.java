package com.example.booking_reportng_system.Service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.models.Booking;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    private final BookingRepository bookingRepository;

    // Dependency Injection of your existing repository
    public ExcelExportService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public ByteArrayInputStream exportBookingsToExcel() throws IOException {
        // Fetch all 1,000+ data rows from the database
        List<Booking> bookings = bookingRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Bookings Raw Data");

            // 1. Define Font Styles for Table Headers
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

            // 2. Setup Excel Column Header Names
            String[] columns = {"Booking No", "Agent", "Country", "Tour Type", "Booking Date", "Amount", "Status"};
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // 3. Loop and Populate All Data Rows Natively
            int rowIndex = 1;
            for (Booking booking : bookings) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(booking.getBookingNo());
                row.createCell(1).setCellValue(booking.getAgent());
                row.createCell(2).setCellValue(booking.getCountry());
                row.createCell(3).setCellValue(booking.getTourType());

                // Safe handling of Date fields converting to string format
                if (booking.getBookingDate() != null) {
                    row.createCell(4).setCellValue(booking.getBookingDate().toString());
                } else {
                    row.createCell(4).setCellValue("");
                }

                row.createCell(5).setCellValue(booking.getAmount());
                row.createCell(6).setCellValue(booking.getStatus());
            }

            // 4. Auto-fit column widths so text does not clip out
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}