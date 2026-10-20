Online Pet Adoption Platform
A Java desktop application for managing pet adoption workflows between adopters, animal shelters, and administrators. The project uses Java Swing for the graphical interface, Java service classes for business logic, JDBC for database connectivity, and MySQL for data storage.

Developed as a team project for third-semester Computer Science coursework.
Table of Contents
- Project Overview
- Features
- Technology Stack
- Architecture
- Project Structure
- Database Design
- Prerequisites
- Setup and Configuration
- Running the Application
- Running the Tests
- Application Workflows
- Business Rules
- Error Handling and Validation
- Security Notes
- Team Contributions
- Academic Project
Project Overview
The Online Pet Adoption Platform is designed to simplify the process of adopting pets by providing a centralized system for adopters, shelters, and administrators.

Adopters can browse pets and submit adoption applications. Shelters can manage pet listings and review adoption requests. Administrators can manage users, moderate pet listings, configure system settings, and view platform information.

The application follows a layered architecture to separate the graphical user interface, business logic, data-access operations, and database models.
Features
Administrator
- Manage user accounts and roles.
- Review pet listings and approve or reject submissions.
- Manage system settings where implemented.
- View platform statistics where implemented.
Shelter
- Add and manage pet listings.
- View adoption applications associated with listed pets.
- Review adoption requests and update their status where supported.
- Communicate with adopters through messaging functionality where implemented.
- View adoption-related statistics where implemented.
Adopter
- Browse available pets.
- View pet details.
- Submit adoption applications.
- Track application status where supported.
- Manage profile information where implemented.
- Review adoption history where implemented.
Note: These bullets describe the project's functional scope. Refer to the current GUI and backend implementation to determine which workflows are fully operational and which are still being developed.
Technology Stack
Technology	Purpose
Java (JDK 17 or later)	Application development
Java Swing	Desktop graphical user interface
JDBC	Communication between Java and MySQL
MySQL 8.0 or later	Relational database
MySQL Connector/J	JDBC driver
Git and GitHub	Version control and team collaboration


Architecture
The application separates presentation, business logic, database operations, and data models.
Java Swing Frontend
        |
        v
Backend Services
        |
        v
Data Access Objects (DAO)
        |
        v
JDBC
        |
        v
MySQL Database
Frontend
The frontend contains the main application window, login screen, dashboards, pet cards, and adoption application dialog. Swing components provide the interface through which users interact with the platform.
Backend Services
The backend contains service interfaces and implementations for operations such as login, user management, pet management, adoption applications, messaging, settings, and platform analytics. Services coordinate application operations, apply business rules, validate inputs, and delegate database work to the DAO layer.
Data Access Layer
The database package contains the JDBC connection utility and DAO classes responsible for database operations. DAOs execute SQL statements and return data to the service layer.
Model Layer
The model package represents application entities and shared definitions used by the frontend, backend, and database layers.
Project Structure
The following is the main source layout in the current project archive. GUI files are grouped into subpackages rather than all being placed directly in src/frontend/.
online-pet-adoption-platform/
|
|-- src/
|   |-- frontend/
|   |   |-- MainFrame.java
|   |   |-- components/
|   |   |   `-- PetCard.java
|   |   |-- utils/
|   |   |   `-- UITheme.java
|   |   `-- views/
|   |       |-- AdminDashboardView.java
|   |       |-- ApplicationFormDialog.java
|   |       |-- LoginView.java
|   |       `-- UserDashboardView.java
|   |
|   |-- backend/
|   |   `-- Service interfaces and implementations
|   |
|   |-- database/
|   |   |-- DatabaseConnection.java
|   |   |-- DAO classes
|   |   `-- DAO and integration tests
|   |
|   `-- model/
|       `-- Application models and shared definitions
|
|-- sql/
|   `-- pet_online_adoption.sql
|
|-- lib/
|   `-- MySQL Connector/J JAR file(s)
|
|-- README.md
|-- .gitignore
`-- .gitattributes
Database Design
The application uses a MySQL database named pet_adoption.
Table	Purpose
Users	Stores user information and roles
Pets	Stores pet details and listing status
AdoptionApplications	Stores adoption requests and their statuses
Messages	Stores messages between shelters and adopters
Settings	Stores application settings


Primary keys and foreign keys maintain relationships between the tables. DAO classes provide the Java application with access to database records.

The database creation and sample-data script is located at sql/pet_online_adoption.sql.

Important: The SQL script drops and recreates the pet_adoption database. Running it again resets the database to the supplied sample data and can delete existing records. Back up any data you need before running it.
Prerequisites
Before running the project, install or prepare:
1. JDK 17 or later.
2. MySQL Server 8.0 or later.
3. MySQL Workbench or another MySQL client.
4. MySQL Connector/J JAR file(s) in the project's lib/ directory.
5. Git, if cloning the repository.
Setup and Configuration
1. Clone the repository
git clone https://github.com/adyajha195-cyber/online-pet-adoption-platform.git
cd online-pet-adoption-platform
2. Configure the database
Open MySQL Workbench and execute sql/pet_online_adoption.sql. This script creates the database, tables, relationships, and sample data.
3. Configure database credentials
The database connection is managed by src/database/DatabaseConnection.java.

The current connection class supports these environment variables:
- DB_URL — database connection URL.
- DB_USER — MySQL username.
- DB_PASSWORD — MySQL password.
For example, in PowerShell:
$env:DB_URL = "jdbc:mysql://localhost:3306/pet_adoption"
$env:DB_USER = "root"
$env:DB_PASSWORD = "your_local_mysql_password"
Replace the example password with your own local MySQL password. These environment variables apply to the current PowerShell session. Do not commit real database credentials to GitHub. The connection class currently includes fallback credentials for local development; before sharing or deploying the project, remove any real password from the source and require credentials to be configured externally.
4. Verify the connector
Ensure that a MySQL Connector/J .jar file is available inside lib/. The commands below use lib/*, so they do not depend on a particular connector filename.
Running the Application
Open PowerShell in the project root directory.
1. Compile the Java source files
javac -d out -cp "lib/*" (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
2. Launch the application
After compilation completes successfully, run:
java -cp "out;lib/*" frontend.MainFrame
Before launching, make sure MySQL Server is running, the pet_adoption database has been created, and the connection credentials are configured correctly.

If compilation fails, resolve the reported errors before launching the application.
Running the Tests
The project includes DAO tests and an integration test for checking database operations and service-layer behavior.
1. Prepare a test database
Execute sql/pet_online_adoption.sql against your local MySQL instance. Use a test database only, because the script recreates the database and its sample data.
2. Compile the source files
Compile all source files so the test classes have access to the model, database, and backend dependencies:
javac -d out -cp "lib/*" (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
3. Run the integration test
java -cp "out;lib/*" database.DaoIntegrationTest
Check the test output for individual PASS or FAIL results. If a test fails, verify the database configuration, table names, sample data, and database constraints before diagnosing the Java code.
Application Workflows
Adopter workflow
1. Launch the application.
2. Log in with an adopter account configured in your local sample database.
3. Browse the available pets.
4. Select a pet and open the adoption application dialog.
5. Enter the required application details.
6. Submit the application.
7. Check the result message and application status, where available.
Shelter workflow
1. Log in with a shelter account configured in your local sample database.
2. Access pet-management functionality.
3. Add or manage pet listings.
4. Review adoption applications associated with the shelter's pets.
5. Update application statuses where supported.
6. Use messaging functionality where available.
Administrator workflow
1. Log in with an administrator account configured in your local sample database.
2. Open the administration dashboard.
3. Access user-management functionality.
4. Review pet listings and moderate them where supported.
5. Access platform settings and statistics where implemented.
Business Rules
The application uses database constraints and DAO/service validation to enforce adoption-related rules.
- Supported user roles: Admin, Shelter, and Adopter.
- Listing statuses: Pending, Approved, and Rejected.
- Pet statuses: Available and Adopted.
- Adoption application statuses: Pending, Approved, and Rejected.
- Message delivery statuses: Sent, Delivered, and Read.
- An adopter may apply only for a pet whose listing is approved and whose pet status is available.
- An adopter may apply for the same pet only once.
Status and role values should use the shared constants defined in model.Status rather than repeated string literals. The database schema and DAO validation should remain consistent with these rules.
Error Handling and Validation
The backend and data-access layers are designed to handle invalid input and database errors separately.
- Input validation checks values before performing operations.
- Invalid role or status values may raise IllegalArgumentException.
- DAO methods propagate SQLException when database operations fail.
- Service-layer exceptions provide a way to communicate application-level failures.
- GUI screens should show understandable error messages instead of exposing raw database details to users.
The exact behavior depends on the implementation of each operation.
Security Notes
- Demo credentials are intended for local testing only.
- Never commit real database passwords or other secrets to a public repository.
- Use environment variables or an appropriate configuration mechanism for local database credentials.
- A production application should store passwords as secure hashes, not plaintext.
- Validate user input and use parameterized SQL statements to reduce SQL injection risks.
Team Contributions
Team member	Primary responsibility
Adya Jha	Database design, SQL, JDBC, DAO layer, and database integration
Rashi	Java Swing GUI and frontend
Eshani	Backend development, OOP, and business logic
Anuskha	Integration and testing


The project is developed collaboratively, with integration between the frontend, backend services, and database layer.
Academic Project
This project is developed as part of third-semester Computer Science coursework. The codebase demonstrates Java Swing, object-oriented programming, JDBC, MySQL database operations, and collaborative software development.
