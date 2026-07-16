package com.example.booking_reportng_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookingReportngSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingReportngSystemApplication.class, args);
	}

}
