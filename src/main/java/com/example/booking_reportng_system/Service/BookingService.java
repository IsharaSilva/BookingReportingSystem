package com.example.booking_reportng_system.Service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.models.Booking;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String importCsv(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        List<Booking> bookingsToSave = new ArrayList<>();

        // Configure CSV layout matching prompt requirements
        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, format)) {

            for (CSVRecord csvRecord : csvParser) {
                long rowNum = csvRecord.getRecordNumber() + 1; // Tracks spreadsheet row for precise error logs
                try {
                    // Extract values from columns based on headers
                    String bookingNo = csvRecord.get("Booking No");
                    String agent = csvRecord.get("Agent");
                    String country = csvRecord.get("Country");
                    String tourType = csvRecord.get("Tour Type");
                    String bookingDateStr = csvRecord.get("Booking Date");
                    String amountStr = csvRecord.get("Amount");
                    String status = csvRecord.get("Status");

                    // 1. Validate Mandatory Fields
                    if (bookingNo == null || bookingNo.isBlank() ||
                            agent == null || agent.isBlank() ||
                            country == null || country.isBlank() ||
                            amountStr == null || amountStr.isBlank() ||
                            bookingDateStr == null || bookingDateStr.isBlank()) {

                        log.error("Row {} Skipped: Missing mandatory values.", rowNum);
                        failureCount++;
                        continue;
                    }

                    // 2. Validate Duplicate Booking Numbers
                    Set<String> processedBookingNumbers = new HashSet<>();

// Inside the loop:
                    if (processedBookingNumbers.contains(bookingNo)) {
                        log.error("Row {} Skipped: Duplicate Booking Number '{}' found within the CSV file.", rowNum, bookingNo);
                        failureCount++;
                        continue;
                    }
                    if (bookingRepository.existsByBookingNo(bookingNo)) {
                        log.error("Row {} Skipped: Duplicate Booking Number '{}' already exists in the database.", rowNum, bookingNo);
                        failureCount++;
                        continue;
                    }

// If valid, remember it
                    processedBookingNumbers.add(bookingNo);

                    // 3. Validate Date format (YYYY-MM-DD)
                    LocalDate bookingDate;
                    try {
                        bookingDate = LocalDate.parse(bookingDateStr);
                    } catch (DateTimeParseException e) {
                        log.error("Row {} Skipped: Invalid date format '{}'. Expected YYYY-MM-DD.", rowNum, bookingDateStr);
                        failureCount++;
                        continue;
                    }

                    // 4. Validate Negative Amounts
                    double amount;
                    try {
                        amount = Double.parseDouble(amountStr);
                        if (amount < 0) {
                            log.error("Row {} Skipped: Amount cannot be negative. Found: {}", rowNum, amount);
                            failureCount++;
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        log.error("Row {} Skipped: Amount '{}' is not a valid number.", rowNum, amountStr);
                        failureCount++;
                        continue;
                    }

                    // Map valid records into Entity array
                    Booking booking = new Booking(bookingNo, agent, country, tourType, bookingDate, amount, status);
                    bookingsToSave.add(booking);
                    successCount++;

                } catch (IllegalArgumentException e) {
                    log.error("Row {} Skipped: Missing dynamic column matching structural headers.", rowNum);
                    failureCount++;
                }
            }

            // Batch execution into MySQL instance
            if (!bookingsToSave.isEmpty()) {
                bookingRepository.saveAll(bookingsToSave);
            }

        } catch (Exception e) {
            log.error("Critical Failure processing CSV structure: {}", e.getMessage());
            return "Failed parsing structural elements completely.";
        }

        return String.format("Processing finished. Successfully imported: %d rows. Skipped: %d invalid rows.", successCount, failureCount);
    }
}