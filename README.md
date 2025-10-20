# DockTracker

DockTracker is a secure, version-controlled document management system built with Java and Spring Boot. It provides a RESTful API for user authentication, document creation, version tracking, and audit logging.

## Features

*   **Secure Authentication**: JWT-based authentication and role-based access control.
*   **Document Versioning**: Track changes to documents by creating and managing multiple versions.
*   **Audit Log**: Automatically logs actions such as document creation and version updates for full traceability.
*   **RESTful API**: A clear and comprehensive API for interacting with the service.
*   **JPA Persistence**: Uses Spring Data JPA for object-relational mapping with a MySQL database.

## Prerequisites

Before you begin, ensure you have the following installed:
*   Java Development Kit (JDK) 17 or later
*   Apache Maven
*   MySQL Server

## Getting Started

Follow these steps to get the application running on your local machine.

### 1. Clone the Repository

```bash
git clone https://github.com/manasarabdullahi/DockTracker.git
cd DockTracker
```

### 2. Configure the Application

Open the `src/main/resources/application.properties` file and update the database details and JWT secret key.

```properties

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Secret Key Configuration
jwt.secretKey=your-secret-jwt-key
```

### 3. Run the Application

You can run the application using the Maven wrapper included in the project.

On macOS/Linux:
```bash
./mvnw spring-boot:run
```

On Windows:
```bash
./mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`.

## API Endpoints

All endpoints require a `Bearer` token in the `Authorization` header, except for `/users/register` and `/users/login`.

### User Management (`/users`)

| Method   | Endpoint                | Description                            |
|----------|-------------------------|----------------------------------------|
| `POST`   | `/users/register`       | Register a new user.                   |
| `POST`   | `/users/login`          | Authenticate and receive a JWT.        |
| `GET`    | `/users`                | Get a list of all users.               |
| `GET`    | `/users/{id}`           | Get a specific user by their ID.       |
| `DELETE` | `/users/delete/{id}`    | Delete a user by their ID.             |

### Document Management (`/documents`)

| Method | Endpoint                             | Description                                            |
|--------|--------------------------------------|--------------------------------------------------------|
| `POST` | `/documents/{userId}`                | Create a new document for the specified user.          |
| `POST` | `/documents/versions/{userId}/{docId}` | Add a new version to an existing document.             |
| `GET`  | `/documents`                         | Get a list of all original documents.                  |
| `GET`  | `/documents/{id}`                    | Get a specific original document by its ID.            |
| `GET`  | `/documents/{id}/versions`           | Get all versions for a specific document.              |
| `GET`  | `/documents/{documentId}/{versionId}`| Get a specific version of a document.                  |
| `GET`  | `/documents/users/{versionId}`       | Get the user (editor) who created a specific version.  |
| `GET`  | `/documents/auditLog/{versionId}`    | Get the audit log associated with a document version.  |
