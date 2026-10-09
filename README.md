# Online Pet Adoption Platform

## About the Project

The Online Pet Adoption Platform is a Java project that aims to make the pet adoption process easier for shelters and people looking to adopt pets.

Shelters can add pets and manage adoption applications, while adopters can browse available pets, apply for adoption, and keep track of their applications. An admin is responsible for managing users, pet listings, system settings, and basic platform information.

The project is being developed as a team project for our 3rd semester.

## Main Features

### Admin
- Manage users and their roles
- Approve or reject pet listings
- Manage system settings
- View basic platform statistics

### Shelter
- Add and manage pet listings
- View and manage adoption applications
- Communicate with adopters
- View adoption statistics

### Adopter
- Browse available pets
- Search for pets by type, breed, and location
- Submit adoption applications
- Track application status
- Manage profile information
- View adoption history

## Technologies Used

- Java
- Java Swing
- MySQL
- JDBC
- Git and GitHub

## Database

We are using MySQL for storing the project data.

The database is called:

`pet_adoption`

The current database contains these tables:

- `Users` - stores admin, shelter, and adopter information
- `Pets` - stores pet details and listing information
- `AdoptionApplications` - stores adoption applications
- `Messages` - stores messages between shelters and adopters
- `Settings` - stores system settings

The SQL script used to create and populate the database is available here:

`sql/pet_online_adoption.sql`

## How to Set Up the Database

1. Install MySQL Server and MySQL Workbench.
2. Clone this repository.
3. Open MySQL Workbench.
4. Tell the app your MySQL password. Either set the `DB_PASSWORD` environment variable (recommended, so you never commit your password), or replace the placeholder "Your_Password" in `DatabaseConnection.java`. `DB_USER` and `DB_URL` can be overridden the same way.
5. Open the `sql/pet_online_adoption.sql` file from this project.
6. Run the SQL script.
7. This will create the `pet_adoption` database, its tables, and sample data.

> The script drops and recreates `pet_adoption`, so running it again resets the database to the sample data.

## Running the Project

The Java application will connect to the MySQL database using JDBC.

Before running the application, make sure:

- MySQL Server is running.
- The `pet_adoption` database has been created.
- The MySQL username and password are correctly configured in the Java database connection code.
- The MySQL JDBC Connector is added to the project.

## Database rules (enforced by the database and the DAOs)

- Roles: `Admin`, `Shelter`, `Adopter`
- Listing status: `Pending`, `Approved`, `Rejected` - new pets start as `Pending`
- Pet status: `Available`, `Adopted`
- Application status: `Pending`, `Approved`, `Rejected`
- Message delivery status: `Sent`, `Delivered`, `Read`
- An adopter can only apply for a pet that is `Approved` and `Available`, and only once per pet
- These strings live in `model.Status` - use the constants instead of typing the text

DAO methods return data (objects, lists, ids, booleans) and throw `SQLException` on database errors. Invalid status/role strings throw `IllegalArgumentException`.

## Running the Tests

1. Run `sql/pet_online_adoption.sql` to get a fresh database.
2. From the project folder (use `;` instead of `:` on Windows):

```text
javac -cp lib/mysql-connector-j-26.7.0.jar -d out src/model/*.java src/database/*.java src/backend/*.java
java -cp out:lib/mysql-connector-j-26.7.0.jar database.DaoIntegrationTest
```
It prints PASS/FAIL for each check and cleans up after itself.


An end-to-end desktop application built with **Java Swing (MVC), JDBC, and MySQL** for managing pet listings, user accounts, and adoption workflows across multiple user roles.

##  User Roles & Login Credentials

| Role        | Email                     | Password     | Primary Capabilities                                                          |
| ----------- | ------------------------- | ------------ | ----------------------------------------------------------------------------- |
| **Admin**   | `admin@petadoption.com`   | `admin123`   | View platform-wide statistics, manage users, approve or reject pet listings   |
| **Shelter** | `shelter@petadoption.com` | `shelter123` | Create pet listings, review incoming applications for listed pets             |
| **Adopter** | `adopter@petadoption.com` | `adopter123` | Browse available pets, submit adoption applications, track application status |

**Note:** These are demo login credentials for testing the application.


## ⚙️ Requirements & Initial Setup

1. **Prerequisites:** Java Development Kit (JDK 17 or later) and MySQL Server 8.0 or later.
2. **JDBC Driver:** Ensure the MySQL Connector/J `.jar` file is placed inside the `lib/` directory.
3. **Database Setup:** Create the `pet_adoption` database and execute the SQL scripts provided in the `sql/` directory.
4. **Database Credentials:** Configure your MySQL username and password in `src/database/DatabaseConnection.java`, or use the environment variables `DB_USER` and `DB_PASSWORD` if the application supports them.

##  How to Launch the Application

Open PowerShell in the project root directory and run:

```powershell
javac -d out -cp "lib/*" (Get-ChildItem -Recurse -Filter *.java src).FullName
if ($?) {
    java -cp "out;lib/*" frontend.MainFrame
}
```

Make sure MySQL is running and the database connection settings are correct before launching the application.

##  Frontend User Guide & Workflows

### 1. Adopter Workflow — Browsing & Applying

1. Launch the application and log in using the demo Adopter credentials: `adopter@petadoption.com` / `adopter123`.
2. Browse the available pets on the User Dashboard.
3. Select a pet and click **Adopt** to open the application dialog.
4. Enter the reason for adoption and click **Submit Application**.
5. Check the confirmation message to verify whether the application was submitted successfully.

### 2. Shelter Workflow — Managing Pets & Applications

1. Log in using the demo Shelter credentials: `shelter@petadoption.com` / `shelter123`.
2. Access the available pet-management and adoption-application features.
3. Review incoming applications and update their status to **Approved** or **Rejected**, if these actions are enabled in the current implementation.

### 3. Admin Workflow — Platform Management

1. Log in using the demo Admin credentials: `admin@petadoption.com` / `admin123`.
2. Open the Admin Dashboard.
3. Review the available platform statistics and user-management features.
4. Review pet listings and approve or reject them, where supported by the application.

##  Resetting Test Data & Re-testing

To test a fresh adoption application, you may need to clear the existing application records.

1. **Stop the application:** Close the GUI window or stop the running process in PowerShell.

2. **Clear the application records:** Run the following command if you intend to delete all adoption applications from the test database.

   ```powershell
   & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p -e "USE pet_adoption; DELETE FROM adoptionapplications;"
   ```

   Enter your MySQL root password when prompted. Adjust the MySQL executable path if it differs on your system.

3. **Relaunch the application:** Run the compilation and launch commands again.

4. **Test the workflow:** Log in as an Adopter and submit a new application.

**Warning:** The deletion command removes every record from `adoptionapplications`. Use it only with test data, and back up any records you need to keep.

##  Notes

* The demo credentials are intended for testing, not production use.
* Ensure that the database schema, Java classes, and documented workflows are consistent.
* Document only the features that are implemented and working in the current version.



## Project Structure

```text
online-pet-adoption-platform/
│
├── src/
│   ├── model/
│   ├── backend/
│   ├── database/
│   └── gui/
│
├── sql/
│   └── pet_online_adoption.sql
│
├── README.md
└── .gitattributes
```

## Current Progress

The MySQL database has been created and tested with sample data.

Completed so far:
- Database and tables
- Primary and foreign keys
- Sample users and pets
- Adoption application data
- Messaging data
- System settings
- Basic SQL queries
- GitHub repository setup
-  Database schema is completed
- SQL file has been added to the repository
- JDBC connection is set up and working
- DAO classes for Users, Pets, Adoption Applications, Messages and Settings are implemented
- Basic login functionality is working
- DAO test files have been added and tested
- MySQL Connector/J has been added to the project
- Database and JDBC files have been pushed to GitHub
- DAOs return data and report errors; validation rules added; `DaoIntegrationTest` covers DAO + service layers
- GUI, backend and integration are currently being worked on

## Team

- **Adya Jha** - Database, SQL, JDBC and Database Integration
- **Rashi** - Java GUI / Frontend
- **Eshani** - Backend / OOP / Business Logic
- **Anuskha** - Integration and Testing

## Academic Project

This project is being developed as part of our 3rd semester academic coursework.
