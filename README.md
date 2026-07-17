# BookingReportingSystem
---

## Tech Stack & Requirements
Language Core:** Java 17
Framework:** Spring Boot 3.x
Database Interface:** MySQL managed via XAMPP (phpMyAdmin)
API Documentation :** OpenAPI Swagger UI
Mail Sandbox Simulator:** Mailtrap (SMTP Debugger)
Document Exporters:** OpenPDF (`com.lowagie.text`) & Apache POI
Frontend :** Single Page Interface (Tailwind CSS v4 & Chart.js Engine) using HTML

---

##Project Installation & Execution
#Initialize Database & Services (XAMPP & phpMyAdmin)
Open the XAMPP Control Panel on your computer.
Click Start next to Apache and MySQL.
Open web browser and navigate to the admin portal:
👉 http://localhost/phpmyadmin/

Create a new database named booking_system_db in phpMyAdmin, or it will  automatically generate the DB when the Spring application is running.

##Build & Boot the Spring Boot Application
Using IDE(Intellij IDEA)
mvn clean install
mvn spring-boot:run
Springboot app work - http://localhost:8080

##Test APIs with OpenAPI Swagger UI
http://localhost:8080/swagger-ui/index.html

##Launch the Front-End UI
Open the directory containing your UI visual file (dashboard.html).
Double-click the file to open it in any web browser

##Emails using Mailtrap Sandbox
Open your browser, navigate to Mailtrap.io, and sign in to your developer dashboard.
Open your configured target Email Testing Inbox.

#For Manual Ad-Hoc Check: Input an email address inside your dashboard UI component and click Send Email, or fire a direct request using the Swagger Panel interface.

#For Automated Background Execution Check: Keep the application engine running active. application properties file is already set to auto-hit.

##Read CSV Data
Goto sample-data folder in bookingReportingSystem; here has booking.csv file.
then using Postman or the swagger api/booking/upload API, click and add that CSV file to read and save in DB.

#Part 1 – Import Booking Data
<img width="1502" height="923" alt="upload csv swagger n1000" src="https://github.com/user-attachments/assets/1b4387e9-8caf-40e2-87b6-9c9002542abd" />





