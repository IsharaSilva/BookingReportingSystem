package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Booking CSV File", description = "Parses an external travel agency booking data sheet. Invalid inputs will be safely logged and omitted.")
    public ResponseEntity<String> uploadCsv(
            @Parameter(description = "Target CSV spreadsheet parsing system configurations", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Validation Failed: Empty upload bundle received.");
        }

        String operationResult = bookingService.importCsv(file);
        return ResponseEntity.ok(operationResult);
    }
}