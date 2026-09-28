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

`sql/pet_adoption.sql`

## How to Set Up the Database

1. Install MySQL Server and MySQL Workbench.
2. Clone this repository.
3. Open MySQL Workbench.
4. Open the `sql/pet_adoption.sql` file from this project.
5. Run the SQL script.
6. This will create the `pet_adoption` database, its tables, and sample data.

## Running the Project

The Java application will connect to the MySQL database using JDBC.

Before running the application, make sure:

- MySQL Server is running.
- The `pet_adoption` database has been created.
- The MySQL username and password are correctly configured in the Java database connection code.
- The MySQL JDBC Connector is added to the project.

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
│   └── pet_adoption.sql
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


## Current Progress

* Database schema is completed
* SQL file has been added to the repository
* JDBC connection is set up and working
* DAO classes for Users, Pets, Adoption Applications, Messages and Settings are implemented
* Basic login functionality is working
* DAO test files have been added and tested
* MySQL Connector/J has been added to the project
* Database and JDBC files have been pushed to GitHub
* GUI, backend and integration are currently being worked on

## Team

- **Adya Jha** - Database, SQL, JDBC and Database Integration
- **Rashi** - Java GUI / Frontend
- **Eshani** - Backend / OOP / Business Logic
- **Anuskha** - Integration and Testing

## Academic Project

This project is being developed as part of our 3rd semester academic coursework.
