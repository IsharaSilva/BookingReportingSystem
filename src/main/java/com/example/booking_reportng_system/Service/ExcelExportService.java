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

    public ExcelExportService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public ByteArrayInputStream exportBookingsToExcel() throws IOException {
        List<Booking> bookings = bookingRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Bookings Raw Data");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

            String[] columns = {"Booking No", "Agent", "Country", "Tour Type", "Booking Date", "Amount", "Status"};
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIndex = 1;
            for (Booking booking : bookings) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(booking.getBookingNo());
                row.createCell(1).setCellValue(booking.getAgent());
                row.createCell(2).setCellValue(booking.getCountry());
                row.createCell(3).setCellValue(booking.getTourType());

                if (booking.getBookingDate() != null) {
                    row.createCell(4).setCellValue(booking.getBookingDate().toString());
                } else {
                    row.createCell(4).setCellValue("");
                }

                row.createCell(5).setCellValue(booking.getAmount() != null ? booking.getAmount() : 0.0);
                row.createCell(6).setCellValue(booking.getStatus() != null ? booking.getStatus() : "");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}