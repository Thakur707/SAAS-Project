**This repository contains a set of Java applications designed to process invoice data retrieved from different file formats and store it into a MySQL database.**

**Overview**
The project includes multiple Java classes, each performing specific tasks in a sequence:

ReadDocFile - This class reads content from a Microsoft Word document (.docx), specifically the word/document.xml file inside the document archive. It then extracts text content from the XML file and writes it to a text (.txt) file.

Read - The Read class reads an invoice file to extract and display information, focusing on the invoice number and details.

ReadAndInsertWithValidation - Reads the text file, validates its content, and inserts data into a MySQL database.

ReadInsertDisplayWithCheck - Reads the file, inserts validated data into the database, and displays the contents of the "file_content" table.

**File Structure**
The project now contains the InvoiceFormat.docx file, which serves as a sample Microsoft Word document containing invoice data. The ReadDocFile class extracts text from this document and writes it to InvoiceFormat.txt.

**How to Use**
ReadDocFile - Extracts content from the provided Word document and writes it to a text file.

**bash**
java ReadDocFile
Read - Displays the invoice number and details from the provided text file.

ReadAndInsertWithValidation - Reads the text file, validates its content, and inserts data into a MySQL database.

ReadInsertDisplayWithCheck - Reads the file, inserts validated data into the database, and displays the contents of the "file_content" table.

**Prerequisites**
Java Development Kit (JDK) - Required to compile and run Java code.
MySQL Database - Ensure the database configuration matches the JDBC URL, username, and password in the Java files for database connectivity.
Setup Instructions
JDK Installation: If not installed, download and install the appropriate JDK for your operating system from the official Oracle website.

**MySQL Database Setup:**

Install MySQL or use an existing MySQL instance.
Create a new database named test.
Update the jdbcURL, username, and password variables in the Java files (ReadAndInsertWithValidation.java and ReadInsertDisplayWithCheck.java) to match your MySQL database configuration.
Compilation:

**Compile the Java classes using the following commands in the command line:**
bash
javac ReadDocFile.java
javac Read.java
javac ReadAndInsertWithValidation.java
javac ReadInsertDisplayWithCheck.java
**Execution:**

Execute the Java classes in the specified sequence using the java command (as described in the "How to Use" section).
Additional Notes
The InvoiceFormat.docx file included in the repository serves as a sample Word document. Modify this file or replace it with your actual invoice data file.

Customize the applications as per your specific use case and requirements.

