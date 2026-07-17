package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailController {

    private final EmailService emailService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/report")
    public ResponseEntity<String> sendReport(@RequestParam("email") String emailString) {
        if (emailString == null || emailString.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email address list cannot be empty.");
        }

        List<String> recipients = Arrays.stream(emailString.split("[,;]"))
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());

        if (recipients.isEmpty()) {
            return ResponseEntity.badRequest().body("Please provide at least one valid email address.");
        }

        for (String email : recipients) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                return ResponseEntity.badRequest().body("Invalid email address found: " + email);
            }
        }

        try {
            emailService.generateAndSendReport(recipients.toArray(new String[0]));
            return ResponseEntity.ok("Emails sent successfully to " + recipients.size() + " recipient(s).");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }
}