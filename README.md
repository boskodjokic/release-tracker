# Release Tracker

[![CI](https://github.com/boskodjokic/release-tracker/actions/workflows/ci.yml/badge.svg)](https://github.com/boskodjokic/release-tracker/actions/workflows/ci.yml)

Simple Spring Boot application for managing software releases.

The application allows creating, updating, deleting and filtering releases.
It includes status workflow validation and persistence using MySQL.

The project is built with:

- Java 21
- Spring Boot 3.x
- Gradle (wrapper included, version 8.5)
- MySQL
- Docker (optional containerized setup)

---

## API

Base URL:

http://localhost:8081/api/releases


You can test API calls using:
- Postman
- curl
- IntelliJ HTTP client

---

# Building and Running the Application

## Option 1 – Run Locally (Without Docker)

### Requirements
- Java 21 installed
- MySQL running locally

On Mac or Linux, you may need to make the wrapper executable.
Navigate to project and run:
```
chmod +x gradlew
```

### 1. Build the project

From the project root:

|OS       | Command               |
|---------|-----------------------|
|Mac/Linux|`./gradlew clean build`|
|Windows  |`gradlew clean build`  |

### 2. Run the application

|OS       | Command           |
|---------|-------------------|
|Mac/Linux|`./gradlew bootRun`|
|Windows  |`gradlew bootRun`  |

OR run the generated jar:
```
java -jar build/libs/ReleaseTracker-0.0.1-SNAPSHOT.jar
```

Application will start on:

http://localhost:8081


---

## Option 2 – Run With Docker (Recommended)

Both application and MySQL database run inside Docker containers.

### 1. Build and start everything

```
docker compose up --build
```

This will:
- Start MySQL
- Build the Spring Boot jar
- Start the application container

Application will be available at:

http://localhost:8081


---

# Database Configuration

When running with Docker:

- Database name: `release_tracker`
- Username: `root`
- Password: `root`
- Port: `3307`

---

# Connecting to MySQL

## Option 1 – Using MySQL CLI

If MySQL is running locally:
```
mysql -u root -p
```

Enter password: `root`

If using Docker:
```
docker exec -it release-mysql mysql -u root -p
```

---

## Option 2 – Using MySQL Workbench (UI)

1. Make sure Docker containers are running:
2Open MySQL Workbench
3Create new connection:
    - Host: `localhost`
    - Port: `3307`
    - Username: `release_user`
    - Password: `release_pass`
3. Click **Test Connection**

4. After connecting, select schema:
- `release_db`

---

## Option 3 – Using IntelliJ Database Tool

1. Open **Database** panel
2. Add new Data Source → MySQL
3. Fill:
    - Host: localhost
    - Port: 3307
    - Database: release_db
    - User: release_user
    - Password: release_pass

4. Test connection

---

# Running Tests

Tests use H2 in-memory database.

Run tests with:

|OS       | Command              |
|---------|----------------------|
|Mac/Linux|`./gradlew clean test`|
|Windows  |`gradlew clean test`  |


Test reports are generated at:

`build/reports/tests/test/index.html`


Open this file in a browser to see detailed results.

---

# Notes

- Status transitions are validated and invalid transitions return HTTP 400.
- Tests are isolated and do not use MySQL.
- Gradle Wrapper is included (version 8.5), so Gradle does not need to be installed separately.
- Docker is optional but recommended for consistent environment setup.
