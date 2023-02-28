### A plan for automating testing of a complex service that interacts with the DBMS and API of the Bank.
**Task:**
Application testing (web-service), providing the possibility of buying a Tour, using 2 ways:
1. Debit card payment.
2. Credit card purchase.

### Auto cases.
**Valid data for the payment form**

- Field "Card number": Entering a numeric value (total 16 characters). 
- Field "Month": Entering two-digit value (From 01 to 12 inclusive), month must be at least the current month if the card's validity year is the current year.
- Field "Year": Entering two-digit value (Year should not have a value less than the value of the current year and not exceed 5 years from the current year)
- Field "Owner": letters of the Latin alphabet at least 1 character.
- Field "CVC/CVV": 3-digit number (from 000 to 999)

**Precondition:**
1. Open the host "Day Trip" http://localhost:8080
2. Choose one of the ways to purchase a tour:
- Option 1: from the card - click the "Buy" button
- Option 2: on credit - click the "Buy on credit" button

**1. Successful display of the form by clicking on the "Buy"/"Buy in credit" button**

Steps:
1. Press the "Buy" button

Expected Result: the form for filling in the fields "Buy by card" is displayed
Actual: Found The Bug*

**2. Successful purchase by clicking on the "Buy"/"Buy in credit" button**
Actual: Found The Bug*

Steps:
1. Click on the "Buy" button
2. Enter a valid number in the "Card number" field (for example: 5536 8573 5764 3967).
3. Enter the number of the month in the "Month" field (for example: 07).
4. Enter the last 2 digits of the year in the "Year" field (for example: 24).
5. Enter a valid Latin name in the "Owner" field (for example: Ilia Erokhin).
6. Enter a 3-digit number (for example 012) in the "CVC/CVV" field.
7. Click on the "Continue" button.

Expected Result: All form fields are filled with valid data. The request is sent successfully.
A pop-up window "Successful" appeared on the screen with the inscription "The operation was approved by the Bank.",
A record with the status APPROVED was added to the database.

**3. Bank rejected card payment (for both forms)**

Steps:
1. Enter an invalid number in the "Card number" field (for example, 1222 2312 2222).
2. Enter valid data in the fields: "Month", "Year", "Owner", "CVC/CVV".
3. Click on the "Continue" button.

Expected Result: All form fields are filled. An error occurred while requesting, a message about an erroneous operation 
appeared on the screen: "Error! The bank refused to carry out the operation", a record with the DECLINED status was added to the database.

**4. Rejected by the bank payment for a non-existent card of a third-party bank (for both forms)**

Steps:
1. Enter the number of a non-existent card of a third-party bank in the "Card number of a third-party bank" field (for example, 4272 3000 0925 5304).
2. Enter valid data in the fields: "Month", "Year", "Owner", "CVC/CVV".
3. Click on the "Continue" button.

Expected Result: All form fields are filled. Error when requesting, on the screen a message about an erroneous operation:
"Error! The bank refused to carry out the operation."

**5. Sending the form "Payment by card" with blank fields (for both forms)**

Steps:
1. Leave form fields blank.
2. Click on the "Continue" button.

Expected Results: Under each of the fields, the message "The field is required" appears, the request does not go away.

**6. Checking the "Card number" field (negative scenario for both forms of payment)**  

Steps:
1. Enter an incorrect number in the "Card number" field (for example: 15 digits, 4444 4444 4444 444).
2. Enter valid data in the fields: "Month", "Year", "Owner", "CVC/CVV".
3. Click on the "Continue" button.

Expected Results: Under the "Card number" field, an error message appears in red "Invalid format", data is not sent.

**7. Checking the "Month" field (negative scenario for both forms of payment)**

Steps:
1. Enter a number in the "Card number" field (for example: 4444 4444 4444 4441).
2. Enter an incorrect month number in the "Month" field (for example: 21).
3. Enter valid data in the fields "Year", "Owner", "CVC/CVV".
4. Click on the "Continue" button.

Expected Result: Under the "Month" field, an error message appears in red "The validity period of the card is incorrect", the data is not sent.

**8. Checking the "Year" field (negative scenario with the past year for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Owner", "CVC/CVV".
2. Enter incorrect last 2 digits of the year in the "Year" field (for example: 20).
3. Click on the "Continue" button.

Expected Result: Under the "Year" field, an error message appears in red "Card expired", data is not sent.

**9. Checking the "Year" field (negative scenario with a year exceeding 5 years for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Owner", "CVC/CVV".
2. Enter incorrect last 2 digits of the year in the "Year" field (for example: 40).
3. Click on the "Continue" button.

Expected Result: Under the "Year" field, an error message appears in red "The validity period of the card is incorrect", the data is not sent.

**10. Data invalidity check (with the expired month of the current year) - for both forms of payment**

Steps:
1. Enter valid data in the fields: "Card number", "Owner", "CVC/CVV".
2. Enter the last month of the current year in the "Month" field (for example: 02)
3. Enter the last 2 digits of the current year (23) in the "Year" field.
4. Click on the "Continue" button.

Expected Result: An error message appears in red "Card expired", data is not sent.

**eleven. Checking the "Owner" field (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Year", "CVC/CVV"
2. Enter an incorrect name in Cyrillic or any name in Cyrillic in the "Owner" field (for example, Илья Ерохин).
3. Enter a 3-digit number in the "CVC/CVV" field (For example: 123).
4. Click on the "Continue" button.

Expected Result: Under the "Owner" field, an error message appears in red "Incorrect format", the data is not sent.

**12. Checking the "CVC/CVV" field (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Year", "Owner"
2. Enter an incorrect number in the "CVC/CVV" field (for example: 000).
3. Click on the "Continue" button.

Expected Result: Under the field "CVC / CVV" an error message appears in red "Invalid format", the data is not sent.

**13. Checking the empty field "Card number" (negative scenario for both forms of payment)**

Steps:
1. Leave the field "Card number" empty.
2. Enter valid data in the fields: "Month", "Year", "Owner", "CVC/CVV".
3. Click on the "Continue" button.

Expected Result: Under the "Card number" field, an error message appears in red "Invalid format", the data is not sent.

**14. Checking the empty field "Month" (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Year", "Owner", "CVC/CVV".
2. Leave the field "Month" empty.
3. Click on the "Continue" button.

OR: Under the field "Month" an error message appears in red "Incorrect format", the data is not sent.

**15. Validation of entering 00 in the "Month" field (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Year", "Owner", "CVC/CVV".
2. Enter 00 in the "Month" field.
3. Click on the "Continue" button.

OR: Under the "Month" field, an error message appears in red "Invalid month format", the data is not sent.

**16. Checking the empty field "Year" (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Owner", "CVC/CVV".
2. Leave the "Year" field blank.
3. Click on the "Continue" button.

Expected Result: Under the "Year" field, an error message appears in red "Field is required", the data is not sent.

**17. Validation of empty field "Owner" (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Year", "CVC/CVV".
2. Leave the "Owner" field blank.
3. Click on the "Continue" button.

Expected Result: Under the "Owner" field, an error message appears in red "Required field", data is not sent.

**18. Validation of the "Owner" field for entering characters (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Year", "CVC/CVV".
2. In the "Owner" field, enter various characters.
3. Click on the "Continue" button.

Expected Result: Under the "Owner" field, an error message appears in red "Incorrect format", the data is not sent.

**19. Checking the empty field "CVC / CVV" (negative scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Year", "Owner".
2. Leave the "CVC/CVV" field blank.
3. Click on the "Continue" button.

Expected Result: Under the "Owner" field, an error message appears in red "Required field", data is not sent.

**20. Checking the click on the "Close" button on the Pop-Up: "Successful" "The operation is approved by the Bank.". (Scenario for both forms of payment)**

Steps:
1. Enter valid data in the fields: "Card number", "Month", "Year", "Owner", "CVC/CVV".
2. Click on the "Continue" button.
3. Click on the cross next to the pop-up window with the message: "Successful" "The operation is approved by the Bank."

Expected Result: Pop-Up closed

**21. Checking for clicking on the "Close" button on the notification: Error! The bank refused to carry out the transaction. (Scenario for both forms of payment)**

Steps:
1. Enter an invalid number in the field: "Card number".
2. Enter valid data in the fields: "Month", "Year", "Owner", "CVC/CVV".
3. Click on the "Continue" button.
4. Click on the cross next to the pop-up window with the message: "Error! The bank refused to carry out the operation"

Expected Result: Pop-up closed.

**Testing the entry of information into databases:**
**22. Checking the creation of a record in the table. (script for both forms)**

Steps:
1. Enter valid card details.
2. Click on the "Continue" button.
3. Click on the cross next to the pop-up window with a successful message
4. Check the database for a record

Expected Result: There is an entry in the table.

**23. Checking the status of a record in a table (Approved map).(script for both forms)**

Steps:
1. Enter valid card details.
2. Click on the "Continue" button.
3. Click on the cross next to the pop-up window with a successful message
4. Check the database for a record with the status "APPROVED"

Expected Result: There is an entry in the table.

**24. "Checking the status of a record in a table (Declined map)." (script for both forms)**

Steps:
1. Enter valid card details.
2. Click on the "Continue" button.
3. Click on the cross next to the pop-up window with a successful message
4. Check the database for a record with the status "DECLINED"

OR: there is an entry in the table.

### Tools Used

- Asus VivoBook 14, core i5 - 10 gen.
- java 11 - Popular programming language version
- Intellij IDEA 2022.3.2 Community edition - an integrated software development environment for many programming languages, including Java
- Gradle - project build automation tool, allows you to easily build a build with all dependencies, and also generates testing reports
- JUnit 5 - popular test framework with nested and parameterized tests
- Selenide - a tool for automating user actions in the browser, focused on the convenience and ease of implementing business logic in autotests
- Faker - a library required to generate random test data, allows you not to invent or manually write test data sets
- Rest Assured - java-library for REST API testing, allows you to automate testing of get and post requests
- Allure - a framework for creating test reports, for a visual display of test passing and errors
- Docker - for deploying virtual database containers, as well as a bank services emulator on NodeJS
- Lombok - a plugin that provides annotations to reduce coding time
- Postman - to check API requests (manual)

### Possible Risks in Automation

1. Lack of documentation and clear technical specifications, which can lead to an error in the operation of a real application
2. The total time spent on automation can exceed the total time of manual testing, and be very labor-intensive
3. The need to support tests (expensive if their number is small)
4. Changes in the web interface of the tested page - it is necessary to make changes to the code of autotests using selenide.
5. Changes in the data of the database tables (fields are added/removed in the table, some fields may change the data type), changes are required in autotests related to Rest Assured.
6. Fall of the test environment or the API used (status 500) - all autotests will fall

### Interval risk assessment (in hours)

- Writing an automation plan: 5 hours;
- Test environment setup: 3 hours;
- Writing and debugging autotests: 19 hours;
- Test run and bug reporting: 3 hours;
- Elimination of remarks: 6 hours;
- Writing a report on the results of testing: 3 hours;
- Preparing a report on the outcome of automation: 1 hour

**Total: 40 hours**
