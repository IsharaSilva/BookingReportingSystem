package com.example.booking_reportng_system.Service;

import com.example.booking_reportng_system.Repository.BookingRepository;
import com.example.booking_reportng_system.dto.DashboardSummaryDTO;
import com.example.booking_reportng_system.models.Booking;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.util.List;

@Service
public class EmailService {

    private final BookingRepository bookingRepository;
    private final JavaMailSender mailSender;

    public EmailService(BookingRepository bookingRepository, JavaMailSender mailSender) {
        this.bookingRepository = bookingRepository;
        this.mailSender = mailSender;
    }

    public void generateAndSendReport(String recipientEmail) throws Exception {
        // 1. Fetch data from DB
        DashboardSummaryDTO summary = bookingRepository.getDashboardSummary();
        List<Booking> allBookings = bookingRepository.findAll();

        // 2. Generate PDF Summary Report in Memory
        byte[] pdfBytes = generatePdfReport(summary, allBookings);

        // 3. Build HTML Email Body (Visualization Layout)
        String htmlBody = "<html><body>" +
                "<h2>📊 Booking Executive Summary Report</h2>" +
                "<p>Hello Team,</p>" +
                "<p>Please find the live operational matrix visualization summary below:</p>" +
                "<table border='1' cellpadding='8' style='border-collapse: collapse; border-color: #eaeaea;'>" +
                "  <tr style='background-color: #f2f2f2;'><th>Metric</th><th>Value</th></tr>" +
                "  <tr><td><b>Total Bookings Processed</b></td><td>" + summary.getTotalBookings() + "</td></tr>" +
                "  <tr><td><span style='color:green;'>Confirmed Bookings</span></td><td>" + summary.getConfirmed() + "</td></tr>" + // Aligned to Lombok DTO
                "  <tr><td><span style='color:red;'>Cancelled Bookings</span></td><td>" + summary.getCancelled() + "</td></tr>" + // Aligned to Lombok DTO
                "  <tr style='background-color: #eaf6ff;'><td><b>Total Aggregated Revenue</b></td><td>$" + String.format("%,.2f", summary.getTotalRevenue()) + "</td></tr>" +
                "</table>" +
                "<p><i>Note: The complete granular operational breakdown ledger sheet is safely attached below as a PDF document wrapper.</i></p>" +
                "</body></html>";

        // 4. Send Email with Attachment
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("Executive Operations Analytics Report");
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

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
        Paragraph title = new Paragraph("Booking Summary Status Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Summary Section
        Font subFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
        document.add(new Paragraph("1. Dashboard Highlights Metrics", subFont));
        document.add(new Paragraph("Total System Volume: " + summary.getTotalBookings()));
        document.add(new Paragraph("Confirmed Records: " + summary.getConfirmed())); // Aligned to Lombok DTO
        document.add(new Paragraph("Cancelled Records: " + summary.getCancelled())); // Aligned to Lombok DTO
        document.add(new Paragraph("Total Volume Valuations: $" + String.format("%,.2f", summary.getTotalRevenue())));
        document.add(new Paragraph("\n"));

        document.close();

        return out.toByteArray();
    }
}
