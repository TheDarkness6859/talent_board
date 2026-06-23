# Talent Board

A comprehensive recruitment management platform built with Spring Boot, designed to streamline the hiring process for both recruiters and job seekers.

## Features

- **Vacancy Management**: Post, view, and manage job vacancies with different statuses (Published, Closed, Draft)
- **Application Tracking**: Submit job applications and track their progress through various states
- **Interview Scheduling**: Schedule and manage interviews with different types (Technical, HR, Behavioral)
- **User Management**: Role-based access control for Admins, Recruiters, and Candidates
- **Email Notifications**: Automated email notifications for application updates and interview scheduling
- **Responsive UI**: Modern, Bootstrap-based interface for seamless user experience

## Tech Stack

- **Backend**: Spring Boot 4.1.0
- **Language**: Java 21
- **Database**: H2 (in-memory database with console access)
- **ORM**: Spring Data JPA
- **Security**: Spring Security 6 with Thymeleaf integration
- **Frontend**: Thymeleaf templates with Bootstrap 5.3.0
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok for reducing boilerplate code
  - Spring Boot DevTools for development
  - Spring Boot Validation for input validation
  - Spring Boot Mail for email functionality

## Project Structure

```
src/main/java/com/talentboard/
├── configuration/          # Security and application configuration
├── controllers/           # REST controllers for web endpoints
├── entities/              # JPA entities (User, Vacancy, Application, Interview)
├── enums/                 # Enumerations (Roles, Status, ApplicationState, etc.)
├── mappers/               # DTO mappers for entity-model conversion
├── models/                # Data Transfer Objects (DTOs)
├── repository/            # Spring Data JPA repositories
└── services/              # Business logic services
```

## Key Entities

- **User**: Represents system users with roles (ADMIN, RECRUITER, CANDIDATE)
- **Vacancy**: Job postings with categories, work modalities, and status
- **Application**: Job applications linking candidates to vacancies with tracking states
- **Interview**: Scheduled interviews with types and associated applications

## Roles and Permissions

- **ADMIN**: Full system access, can manage vacancies and users
- **RECRUITER**: Can create and manage vacancies, review applications, schedule interviews
- **CANDIDATE**: Can view vacancies, submit applications, track their applications and interviews

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- (Optional) Docker for containerized deployment

## Installation

### Local Development

1. Clone the repository:
```bash
git clone <repository-url>
cd talentBoard
```

2. Configure application properties:
```bash
cp src/main/resources/application.properties.template src/main/resources/application.properties
```

3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`

### Docker Deployment

1. Build the Docker image:
```bash
docker build -t talentboard .
```

2. Run the container:
```bash
docker run -p 8080:8080 talentboard
```

## Accessing H2 Console

When running locally, access the H2 database console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## Configuration

Key configuration options in `application.properties`:

- Database settings (H2 configuration)
- Mail server settings for email notifications
- Security settings for authentication and authorization
- Server port and context path

## API Endpoints

### Public
- `GET /` - Home page
- `GET /vacancies` - View all vacancies
- `GET /login` - Login page
- `GET /users/register` - Registration page

### Authenticated Users
- `GET /applications/my` - View my applications
- `GET /interviews/my` - View my interviews
- `GET /users/profile` - User profile
- `POST /logout` - Logout

### Recruiters & Admins
- `GET /vacancies/create` - Create vacancy form
- `POST /vacancies` - Submit new vacancy
- `GET /vacancies/{id}/edit` - Edit vacancy
- `POST /vacancies/{id}` - Update vacancy
- `POST /vacancies/{id}/delete` - Delete vacancy

## Development

### Running Tests
```bash
./mvnw test
```

### Using DevTools
The project includes Spring Boot DevTools for automatic restart on file changes during development.

## License

© 2026 Talent Board. All rights reserved.
