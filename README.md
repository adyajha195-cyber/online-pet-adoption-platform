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
