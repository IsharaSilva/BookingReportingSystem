package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/report")
    @Operation(summary = "Generate Report and Send Email", description = "Generates a real-time system breakdown PDF report and sends it over to target SMTP relays.")
    public ResponseEntity<String> sendReport(@RequestParam("email") String email) {
        try {
            emailService.generateAndSendReport(email);
            return ResponseEntity.ok("Operational Summary Analytics Report dispatched successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed processing mail distribution components: " + e.getMessage());
        }
    }
}
