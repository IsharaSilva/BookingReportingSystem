package com.example.booking_reportng_system.Service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.dto.DashboardSummaryDTO;
import com.example.booking_reportng_system.models.Booking;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Arrays; // Added missing import
import java.util.List;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private final BookingRepository bookingRepository;
    private final JavaMailSender mailSender;

    @Value("${app.report.recipient:manager@travel.com}")
    private String defaultRecipient;

    public EmailService(BookingRepository bookingRepository, JavaMailSender mailSender) {
        this.bookingRepository = bookingRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(cron = "${app.report.cron}")
    public void sendDailyAutomatedReport() {
        LOGGER.info("Executing automated daily report scheduling...");
        try {
            // Split the default recipient configuration in case it contains multiple emails
            String[] recipients = Arrays.stream(defaultRecipient.split("[,;]"))
                    .map(String::trim)
                    .filter(e -> !e.isEmpty())
                    .toArray(String[]::new);

            generateAndSendReport(recipients);
            LOGGER.info("Automated daily report successfully delivered.");
        } catch (Exception e) {
            LOGGER.severe("CRITICAL: Failed to send scheduled daily report: " + e.getMessage());
        }
    }

    public void generateAndSendReport(String[] recipientEmails) throws Exception {
        if (recipientEmails == null || recipientEmails.length == 0) {
            throw new IllegalArgumentException("Recipient email array cannot be empty");
        }

        DashboardSummaryDTO summary = bookingRepository.getDashboardSummary();
        List<Booking> allBookings = bookingRepository.findAll();

        byte[] pdfBytes = generatePdfReport(summary, allBookings);

        String htmlBody = """
            <html>
            <body>
                <h2>📊 Booking Summary Report</h2>
                <p>Hello Team,</p>
                <p>Please find the booking visualization summary below:</p>
                <table border="1" cellpadding="8" style="border-collapse: collapse; border-color: #eaeaea;">
                    <tr style="background-color: #f2f2f2;"><th>Metric</th><th>Value</th></tr>
                    <tr><td><b>Total Bookings</b></td><td>%d</td></tr>
                    <tr><td><span style="color:green;">Confirmed Bookings</span></td><td>%d</td></tr>
                    <tr><td><span style="color:red;">Cancelled Bookings</span></td><td>%d</td></tr>
                    <tr><td><span style="color:orange;">Pending Bookings</span></td><td>%d</td></tr>
                    <tr style="background-color: #eaf6ff;"><td><b>Total Revenue</b></td><td>$%s</td></tr>
                </table>
                <p><i>The complete booking summary sheet is attached below as a PDF document.</i></p>
            </body>
            </html>
            """.formatted(
                summary.getTotalBookings(),
                summary.getConfirmed(),
                summary.getCancelled(),
                summary.getPending(),
                String.format("%,.2f", summary.getTotalRevenue())
        );

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipientEmails);
        helper.setSubject("Booking Summary Report");
        helper.setText(htmlBody, true);
        helper.setFrom("analytics@travelcorp.com");
        helper.addAttachment("Booking_Summary_Report.pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }

    private byte[] generatePdfReport(DashboardSummaryDTO summary, List<Booking> bookings) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Booking Summary Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        Font subFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("Dashboard Visualization", subFont));
        document.add(new Paragraph("Total Bookings: " + summary.getTotalBookings()));
        document.add(new Paragraph("Confirmed Bookings: " + summary.getConfirmed()));
        document.add(new Paragraph("Cancelled Bookings: " + summary.getCancelled()));
        document.add(new Paragraph("Pending Bookings: " + summary.getPending()));
        document.add(new Paragraph("Total Revenue: $" + String.format("%,.2f", summary.getTotalRevenue())));

        document.close();
        return out.toByteArray();
    }
}