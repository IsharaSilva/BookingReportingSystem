package com.example.booking_reportng_system.Controller;

import com.example.booking_reportng_system.Service.ExcelExportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*") // Allows your separate HTML files to talk to this endpoint safely
public class ExcelReportController {

    private final ExcelExportService excelExportService;

    public ExcelReportController(ExcelExportService excelExportService) {
        this.excelExportService = excelExportService;
    }

    @GetMapping("/download")
    @Operation(summary = "Download Bookings Excel Sheet", description = "Generates and downloads a clean workbook ledger containing all raw transactional rows.")
    public ResponseEntity<InputStreamResource> downloadExcel() {
        try {
            ByteArrayInputStream in = excelExportService.exportBookingsToExcel();

            // Set download file headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=bookings_ledger_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(in));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}