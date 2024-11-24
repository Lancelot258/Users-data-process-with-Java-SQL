# Users-data-process-with-Java-SQL
Read users' data from regular forms, like .csv. Transfer data to database with the same types, and then display a control panel ,which is used to oeprate specific data.

# Customer Management Application

## Overview
This project is a Java-based Customer Management Application that provides functionalities for managing customer information stored in a database. The application includes features to:

- Load customer data from a CSV file into an SQLite database.
- Display customer information in a graphical user interface (GUI) using Swing.
- Filter, update, and delete customer records.
- Support dynamic queries based on user input.

The project integrates backend data processing with a user-friendly interface for customer management.

## Features

1. **CSV Data Ingestion:**
   - Load customer data from a structured CSV file into an SQLite database.
   - Automatically create database tables if they do not already exist.
   - Handle duplicate records using unique constraints (e.g., email).

2. **Graphical User Interface (GUI):**
   - Display customer records in a tabular format.
   - Enable filtering of customer data based on multiple criteria (e.g., name, city, country, date range).
   - View and update individual customer records.
   - Delete customer records directly from the GUI.

3. **Database Operations:**
   - Efficiently query and update SQLite database tables.
   - Use parameterized SQL statements to prevent SQL injection.

4. **Error Handling:**
   - Validate input data during CSV ingestion.
   - Handle database connection and query errors gracefully.

## Project Structure

```
.
├── .settings/
│   ├── org.eclipse.core.resources.prefs
│   └── org.eclipse.jdt.core.prefs
├── src/
│   ├── Customer.java
│   ├── CustomerDisplay.java
│   ├── DisplayList.java
│   └── IngestCSV.java
├── .classpath
├── .gitignore
├── LICENSE
├── README.md
├── customers-100.csv
```

### Key Files and Directories

- **src/Customer.java**
  - Defines the `Customer` class, which represents a customer entity with attributes like `customerId`, `firstName`, `lastName`, `email`, and more.
  - Includes getter and setter methods, a constructor, and a `toString` method for easy representation.

- **src/CustomerDisplay.java**
  - Implements the main GUI for displaying customer data in a table.
  - Allows filtering of customer records using various criteria.
  - Provides options to view, update, and delete individual customer records.

- **src/DisplayList.java**
  - Provides a detailed view of a single customer record.
  - Allows updating or deleting a specific customer.

- **src/IngestCSV.java**
  - Handles the ingestion of customer data from a CSV file.
  - Creates the `testdb` SQLite table if it does not exist.
  - Inserts data into the database, ensuring uniqueness of records based on email.

- **customers-100.csv**
  - A sample CSV file containing customer data for testing and demonstration purposes.

- **.gitignore**
  - Specifies files and directories to be ignored by Git, such as `.settings` and `.classpath`.

- **README.md**
  - Documentation file explaining the project’s purpose, features, and usage.

## Prerequisites

- Java Development Kit (JDK) 8 or higher.
- SQLite installed locally.
- Eclipse IDE (optional, for project development).

## How to Run

### 1. Set Up the Database
   - Ensure SQLite is installed.
   - Update the database path in the `IngestCSV` and `CustomerDisplay` classes.
   - Run `IngestCSV.java` to create the database table and load data from `customers-100.csv`.

### 2. Start the Application
   - Compile and run `CustomerDisplay.java` to launch the main GUI.
   - Use the GUI to filter, update, or delete customer records.

### 3. Update and Delete Records
   - Select a customer record from the table to view detailed information.
   - Use the `Update` button to modify the record.
   - Use the `Delete` button to remove the record from the database.

## Future Improvements

- Add support for exporting filtered data back to a CSV file.
- Implement user authentication for enhanced security.
- Add pagination for large datasets.
- Enhance the GUI design for better usability.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

