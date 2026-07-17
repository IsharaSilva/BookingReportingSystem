package com.example.booking_reportng_system.Service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.models.Booking;
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
        log.info("Starting CSV import process for file: {}", file.getOriginalFilename());
        int successCount = 0;
        int failureCount = 0;
        List<Booking> bookingsToSave = new ArrayList<>();
        Set<String> processedBookingNumbers = new HashSet<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String headerLine = fileReader.readLine();
            if (headerLine == null) {
                log.warn("CSV import aborted: The uploaded file has no headers or data.");
                return "Validation Failed: Empty file uploaded.";
            }

            String[] headers = headerLine.split(",");
            int idxBookingNo = -1, idxAgent = -1, idxCountry = -1, idxTourType = -1, idxDate = -1, idxAmount = -1, idxStatus = -1;

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].replace("\"", "").trim();
                if (header.equalsIgnoreCase("Booking No")) idxBookingNo = i;
                else if (header.equalsIgnoreCase("Agent")) idxAgent = i;
                else if (header.equalsIgnoreCase("Country")) idxCountry = i;
                else if (header.equalsIgnoreCase("Tour Type")) idxTourType = i;
                else if (header.equalsIgnoreCase("Booking Date")) idxDate = i;
                else if (header.equalsIgnoreCase("Amount")) idxAmount = i;
                else if (header.equalsIgnoreCase("Status")) idxStatus = i;
            }

            String line;
            long rowNum = 1;

            while ((line = fileReader.readLine()) != null) {
                rowNum++;

                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                try {
                    String bookingNo = cleanToken(tokens[idxBookingNo]);
                    String agent = cleanToken(tokens[idxAgent]);
                    String country = cleanToken(tokens[idxCountry]);
                    String tourType = cleanToken(tokens[idxTourType]);
                    String bookingDateStr = cleanToken(tokens[idxDate]);
                    String amountStr = cleanToken(tokens[idxAmount]);
                    String status = cleanToken(tokens[idxStatus]);

                    // Validate Mandatory Fields
                    if (bookingNo.isBlank() || agent.isBlank() || country.isBlank() || amountStr.isBlank() || bookingDateStr.isBlank()) {
                        log.error("Row {} Skipped: Missing mandatory values.", rowNum);
                        failureCount++;
                        continue;
                    }

                    // Validate Duplicate Booking Numbers
                    if (processedBookingNumbers.contains(bookingNo)) {
                        log.error("Row {} Skipped: Duplicate Booking Number '{}' found within the CSV file.", rowNum, bookingNo);
                        failureCount++;
                        continue;
                    }

                    // Validate Duplicate Booking Numbers against the Database
                    if (bookingRepository.existsByBookingNo(bookingNo)) {
                        log.error("Row {} Skipped: Duplicate Booking Number '{}' already exists in the database.", rowNum, bookingNo);
                        failureCount++;
                        continue;
                    }

                    processedBookingNumbers.add(bookingNo);

                    // Validate Date format (YYYY-MM-DD)
                    LocalDate bookingDate;
                    try {
                        bookingDate = LocalDate.parse(bookingDateStr);
                    } catch (DateTimeParseException e) {
                        log.error("Row {} Skipped: Invalid date format '{}'. Expected YYYY-MM-DD.", rowNum, bookingDateStr);
                        failureCount++;
                        continue;
                    }

                    // Validate Negative Amounts
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

                    Booking booking = new Booking(bookingNo, agent, country, tourType, bookingDate, amount, status);
                    bookingsToSave.add(booking);
                    successCount++;

                } catch (Exception e) {
                    log.error("Row {} Skipped: Processing structure error occurred.", rowNum);
                    failureCount++;
                }
            }

            if (!bookingsToSave.isEmpty()) {
                bookingRepository.saveAll(bookingsToSave);
            }

        } catch (Exception e) {
            log.error("Critical Failure processing CSV structure: {}", e.getMessage());
            return "Failed parsing structural elements completely.";
        }
        log.info("CSV Import summary -> Total Processed: {}. Saved: {}. Skipped: {}.", (successCount + failureCount), successCount, failureCount);
        return String.format("Processing finished. Successfully imported: %d rows. Skipped: %d invalid rows.", successCount, failureCount);
    }

    private String cleanToken(String token) {
        if (token == null) return "";
        return token.replace("\"", "").trim();
    }
}