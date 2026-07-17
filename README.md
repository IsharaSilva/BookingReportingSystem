# BookingReportingSystem
---------------------------------------------------------

## Tech Stack & Requirements

**Language Core: Java 17
**Framework: Spring Boot
**Database Interface:MySQL managed via XAMPP (phpMyAdmin)
**API Documentation : OpenAPI Swagger UI
**Mail Sandbox Simulator: Mailtrap (SMTP Debugger)
**Document Exporters: OpenPDF (`com.lowagie.text`) & Apache POI
**Frontend : Single Page Interface (Tailwind CSS v4 & Chart.js Engine) using HTML

----------------------------------------------------------

**##Project Installation & Execution**

#Initialize Database & Services (XAMPP & phpMyAdmin)

Open the XAMPP Control Panel on your computer.
Click Start next to Apache and MySQL.
Open web browser and navigate to the admin portal:
👉 http://localhost/phpmyadmin/

Create a new database named booking_system_db in phpMyAdmin, or it will  automatically generate the DB when the Spring application is running.

**##Build & Boot the Spring Boot Application**

Using IDE(Intellij IDEA)

#mvn clean install

#mvn spring-boot:run

#Springboot app work - **http://localhost:8080**

**##Test APIs with OpenAPI Swagger UI**

http://localhost:8080/swagger-ui/index.html

**##Launch the Front-End UI**

Open the directory containing UI visual file (**dashboard.html**).
Double-click the file to open it in any web browser

**##Emails using Mailtrap Sandbox**

Open your browser, navigate to Mailtrap.io, and sign in to your developer dashboard.

Open your configured target Email Testing Inbox.

#For Manual Ad-Hoc Check: Input an email address inside your dashboard UI component and click Send Email, or fire a direct request using the Swagger Panel interface.

#For Automated Background Execution Check: Keep the application engine running active. application properties file is already set to auto-hit.

**##Read CSV Data**
Goto sample-data folder in bookingReportingSystem; here has booking.csv file.
then using Postman or the swagger api/booking/upload API, click and add that CSV file to read and save in DB.


--------------------------------------------------


**#Part 1 – Import Booking Data**


** use Swagger to upload the csv file

<img width="1502" height="923" alt="upload csv swagger n1000" src="https://github.com/user-attachments/assets/1b4387e9-8caf-40e2-87b6-9c9002542abd" />



** read that data using MYSQL

<img width="1510" height="1030" alt="csv read data to db" src="https://github.com/user-attachments/assets/36193c00-a50b-4cc2-92fb-ff1e6484fed5" />


------------------------------------------------------


**#Part 2 – Dashboard API**

<img width="1611" height="869" alt="swagger APIs" src="https://github.com/user-attachments/assets/dcea6e7b-c8e2-48b8-afd1-739c79984b3a" />

**Total Bookings
GET /api/dashboard/summary

<img width="1491" height="879" alt="TotalSummary" src="https://github.com/user-attachments/assets/03cde3fb-6e44-4d4a-a707-1dda8cf30cfa" />


**Revenue by Country
GET /api/dashboard/revenue-country

<img width="1465" height="893" alt="revenue by country" src="https://github.com/user-attachments/assets/897c260f-7b71-40bc-a80e-341190edc070" />


**Bookings by Agent
GET /api/dashboard/agent-bookings

<img width="1494" height="906" alt="agents with booking count" src="https://github.com/user-attachments/assets/7891ba08-a01c-416c-b29c-1b6384277efe" />


**Monthly Revenue Trend
GET /api/dashboard/monthly-revenue

<img width="1480" height="927" alt="monthly revenue" src="https://github.com/user-attachments/assets/47e431cf-e273-48dc-9975-97db0acd5f4b" />


------------------------------------------------------


**#Part 3 – Data Visualization**

<img width="1688" height="1028" alt="Booking system UI" src="https://github.com/user-attachments/assets/776cbb4a-5955-4076-9bc3-af15e5fd2ab9" />


**KPI Cards

<img width="1308" height="418" alt="KPI cards UI" src="https://github.com/user-attachments/assets/8dfd6a2c-8db1-49a9-a84c-e2cc53467b20" />


**Charts 

<img width="1279" height="796" alt="Charts UI" src="https://github.com/user-attachments/assets/a578c3d3-d47e-43f1-8059-05ffbe503a40" />


---------------------------------------------------


**#Part 4 – Email Sending with attachments**

**Support for multiple recipients**__

**Create an API
POST /api/email/report

<img width="1533" height="907" alt="email sent by swagger" src="https://github.com/user-attachments/assets/75382df3-953e-4059-91e2-85e8b3887e14" />


** received email by mailstrap
<img width="1870" height="752" alt="received email by mailstrap" src="https://github.com/user-attachments/assets/5558fb44-8940-4635-b052-88a4a7f5ed84" />



##Automatically generate Email with attachment
<img width="1327" height="295" alt="emaill automate in IDE" src="https://github.com/user-attachments/assets/a1ca02c2-63e5-4ebc-95e4-cd23404305ed" />



** Received an email via MailsTrap automatically
<img width="1849" height="664" alt="emill automate generate mailtrap" src="https://github.com/user-attachments/assets/2e56cd11-31a4-4722-9764-861038368337" />


----------------------------------------------


**#Unit Tests**

**unit test for 3 services

<img width="1361" height="549" alt="run test" src="https://github.com/user-attachments/assets/a1732286-97fc-4ec0-a57d-860e9b534ea5" />



------------------------------------------------


**#Bonus Tasks**

Excel export & CI/CD (Github Actions)

##**Excel Export API**

<img width="1486" height="901" alt="Excel generate" src="https://github.com/user-attachments/assets/7ee4505d-41e8-4ee8-bf31-971ecfe1f324" />



##**Excel Export UI Button**

<img width="1688" height="1028" alt="Booking system UI" src="https://github.com/user-attachments/assets/1e5dd235-6b3c-4274-9ee5-bcfee4b6947f" />



##**CI/CD (GitHub Actions) **

<img width="1874" height="685" alt="CICD github actions" src="https://github.com/user-attachments/assets/54cb21c6-9e53-463a-9f1b-6259eb4d2082" />


