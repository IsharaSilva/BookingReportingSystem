package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bookings")
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Booking CSV File")
    public ResponseEntity<String> uploadCsv(
            @Parameter(description = "Target CSV spreadsheet parsing system configurations", required = true)
            @RequestParam("file") MultipartFile file) {

        log.info("REST Request received at /api/bookings/upload. Content size: {} bytes", file.getSize());

        if (file.isEmpty()) {
            log.warn("Rejected incoming upload: File bundle parameter payload is empty.");
            return ResponseEntity.badRequest().body("Validation Failed: Empty upload bundle received.");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("text/csv") && !contentType.equals("application/vnd.ms-excel"))) {
            log.warn("Rejected upload with invalid MIME type: {}", contentType);
            return ResponseEntity.badRequest().body("Validation Failed: Only CSV files are allowed.");
        }

        String operationResult = bookingService.importCsv(file);
        return ResponseEntity.ok(operationResult);
    }
}