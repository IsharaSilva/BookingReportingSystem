package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/report")
    public ResponseEntity<String> sendReport(@RequestParam("email") String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return ResponseEntity.badRequest().body("Please enter a valid email address.");
        }

        try {
            emailService.generateAndSendReport(email.trim());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }
}

