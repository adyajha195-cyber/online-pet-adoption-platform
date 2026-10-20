# Online Pet Adoption Platform

A Java Swing desktop application that helps manage the pet adoption process using Java, JDBC, and MySQL.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Database](#database)
- [Setup and Installation](#setup-and-installation)
- [Compile and Run](#compile-and-run)
- [Running Tests](#running-tests)
- [Business Rules](#business-rules)
- [Security Notes](#security-notes)
- [Team Contributions](#team-contributions)

## Overview

The Online Pet Adoption Platform is a team project developed using Java Swing for the graphical user interface and MySQL for database storage.

The application follows a layered structure that separates the frontend, backend services, database access objects (DAOs), and data models. JDBC is used to connect the Java application to the MySQL database.

## Features

### Adopter

- Browse available pets.
- View pet information.
- Submit adoption applications.
- Check application status where supported by the current implementation.

### Shelter

- Manage pet listings where implemented.
- Review adoption applications where implemented.
- Communicate with adopters where messaging features are available.

### Administrator

- Access the administration dashboard.
- Manage platform information supported by the current implementation.
- Access administrative settings and analytics where implemented.

> **Note:** The features above describe the project scope. Their availability depends on the current implementation and integration status.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java (JDK 17+) | Application development |
| Java Swing | Graphical user interface |
| JDBC | Database connectivity |
| MySQL | Database management |
| MySQL Connector/J | JDBC driver |
| Git and GitHub | Version control and collaboration |

## Project Structure

```text
online-pet-adoption-platform/
├── src/
│   ├── frontend/
│   │   ├── MainFrame.java
│   │   ├── LoginView.java
│   │   ├── UserDashboardView.java
│   │   ├── AdminDashboardView.java
│   │   ├── PetCard.java
│   │   ├── ApplicationFormDialog.java
│   │   └── UITheme.java
│   ├── backend/
│   │   └── Service interfaces and implementations
│   ├── database/
│   │   ├── DatabaseConnection.java
│   │   ├── DAO classes
│   │   └── DAO and integration tests
│   └── model/
│       └── Application models and shared definitions
├── sql/
│   └── pet_online_adoption.sql
├── lib/
│   └── MySQL Connector/J JAR
└── README.md
```

## Database

The SQL script used to set up the database is located at:

```text
sql/pet_online_adoption.sql
```

The database is named `pet_adoption` and includes the following tables:

| Table | Purpose |
|---|---|
| `Users` | Stores user account information and roles |
| `Pets` | Stores pet details and listing information |
| `AdoptionApplications` | Stores adoption applications and their statuses |
| `Messages` | Stores communication between users |
| `Settings` | Stores application settings |

**Important:** The SQL script drops and recreates the `pet_adoption` database. Running it again may delete existing data. Back up any important data before executing the script.

## Setup and Installation

### 1. Prerequisites

Install or prepare the following:

- JDK 17 or later
- MySQL Server
- MySQL Workbench or another MySQL client
- MySQL Connector/J
- Git (if cloning the repository)

### 2. Clone the Repository

Open PowerShell and run:

```powershell
git clone https://github.com/adyajha195-cyber/online-pet-adoption-platform.git
cd online-pet-adoption-platform
```

### 3. Set Up the Database

1. Open MySQL Workbench.
2. Open `sql/pet_online_adoption.sql`.
3. Execute the script to create the database and its tables.

Remember that the script recreates the database, so do not run it against data you need to preserve.

### 4. Configure the Database Connection

Open the following file:

```text
src/database/DatabaseConnection.java
```

Configure the database URL, username, and password to match your local MySQL installation.

If the connection class supports environment variables, you can configure them in PowerShell:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/pet_adoption"
$env:DB_USER = "root"
$env:DB_PASSWORD = "your_local_mysql_password"
```

Replace the example password with your local MySQL password. Use the configuration method supported by the actual `DatabaseConnection.java` implementation.

**Do not commit real database credentials to GitHub.**

### 5. Check the JDBC Driver

Ensure that the MySQL Connector/J `.jar` file is present in the `lib/` directory.

## Compile and Run

Run these commands from the project's root directory in PowerShell.

### Compile the Java Files

```powershell
javac -d out -cp "lib/*" (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

### Launch the Application

```powershell
java -cp "out;lib/*" frontend.MainFrame
```

Before launching the application, make sure that:

- MySQL Server is running.
- The `pet_adoption` database has been created.
- The database credentials are configured correctly.
- The JDBC driver is present in `lib/`.

## Running Tests

The project includes DAO and database integration tests.

First, compile the Java source files:

```powershell
javac -d out -cp "lib/*" (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

Then run the DAO integration test:

```powershell
java -cp "out;lib/*" database.DaoIntegrationTest
```

Review the test output for `PASS` or `FAIL` results.

The integration test requires a working database connection and a correctly initialized database schema. If it fails, check the connection settings, database tables, sample data, and constraints.

## Business Rules

The application uses roles and status values such as:

- **User roles:** Admin, Shelter, Adopter
- **Listing statuses:** Pending, Approved, Rejected
- **Pet statuses:** Available, Adopted
- **Adoption application statuses:** Pending, Approved, Rejected
- **Message statuses:** Sent, Delivered, Read

The intended adoption workflow allows an adopter to apply for an approved, available pet. Duplicate applications for the same pet should be prevented.

The exact enforcement of these rules depends on the current DAO and service implementations.

## Security Notes

- Do not commit database passwords or other secrets.
- Use parameterized SQL statements to help prevent SQL injection.
- Passwords should be stored as secure hashes rather than plaintext.
- Avoid exposing raw database errors to end users.
- Use test credentials only in local development environments.

## Team Contributions

| Team Member | Responsibility |
|---|---|
| **Adya Jha** | Database design, SQL, JDBC, DAO layer, and database integration |
| **Rashi** | Java Swing GUI and frontend development |
| **Eshani** | Backend development, OOP, and business logic |
| **Anuskha** | Integration and testing |

## Academic Project

Developed as part of third-semester Computer Science coursework.



