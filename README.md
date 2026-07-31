# Governance Policy Management System
A Spring Boot based Governance Policy Management System that manages policy creation, submission, approval, and rejection workflows. The system follows a microservice-oriented and event-driven architecture using Apache Kafka for asynchronous communication between services.

---

# Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [System Workflow](#system-workflow)
- [Services Description](#services-description)
- [Database Design](#database-design)
- [API Documentation](#api-documentation)
- [Kafka Event Flow](#kafka-event-flow)
- [Testing](#testing)
- [How to Run the Application](#how-to-run-the-application)
- [Future Improvements](#future-improvements)

---

# Project Overview

The Governance Policy Management System is a backend application designed to manage organizational policies throughout their lifecycle.

The system allows users to:

- Create new policies
- View existing policies
- Retrieve policies by ID
- Submit policies for approval
- Approve policies
- Reject policies

The application follows an event-driven architecture where policy changes generate events that are consumed by other services, such as the Audit Service.

---

# Architecture

The system is designed using a microservice architecture with asynchronous communication through Apache Kafka.

## High-Level Architecture


Client
   |
   |
   v

Governance Service
   |
   |
   | Publish Policy Events
   |
   v

Apache Kafka
   |
   |
   | Consume Events
   |
   v

Audit Service


---

## Architecture Explanation

### Governance Service

The Governance Service is the main business service responsible for:

- Managing policies
- Applying policy lifecycle rules
- Persisting policy data
- Publishing policy events


### Kafka Message Broker

Apache Kafka acts as an event communication layer between services.

Instead of directly calling another service, Governance Service publishes events to Kafka topics.

This provides:

- Loose coupling
- Better scalability
- Reliable event communication


### Audit Service

The Audit Service consumes policy events from Kafka and stores audit records.

It tracks:

- Who created a policy
- When a policy changed
- What action happened

# Instructions to Run the System

## Prerequisites

Before running the application, ensure the following software is installed on your machine:

- Java 21
- Maven 3.9+
- Docker Desktop
- PostgreSQL
- IntelliJ IDEA (Recommended)
- Git

---

## Step 1 – Clone the Repository

```bash
git clone https://github.com/yourusername/backend-internship.git

cd backend-internship
```

---

## Step 2 – Start Docker

Open **Docker Desktop** and wait until the Docker Engine is running.

Verify Docker installation:

```bash
docker version
```

---

## Step 3 – Start Kafka

Run Docker Compose:

```bash
docker compose up -d
```

Verify that the containers are running:

```bash
docker ps
```

Expected containers:

- Kafka
- Kafka UI

---

## Step 4 – Create PostgreSQL Databases

Create the following databases in PostgreSQL:

- `governance_db`
- `audit_db`

---

## Step 5 – Configure `application.properties`

### Governance Service

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/governance_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.kafka.bootstrap-servers=localhost:9092
```

### Audit Service

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/audit_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.kafka.bootstrap-servers=localhost:9092
```

---

## Step 6 – Build the Projects

### Governance Service

```bash
mvn clean install
```

### Audit Service

```bash
mvn clean install
```

---

## Step 7 – Start the Audit Service

Start the Audit Service first so it can consume Kafka events.

Using Maven:

```bash
mvn spring-boot:run
```

Or simply run the project from IntelliJ IDEA.

---

## Step 8 – Start the Governance Service

Using Maven:

```bash
mvn spring-boot:run
```

Or run the project from IntelliJ IDEA.

---

## Step 9 – Test the REST APIs

### Create Policy

**POST**

```
/api/policies
```

Request Body

```json
{
  "title": "Security Policy",
  "description": "Company security policy",
  "createdBy": "admin"
}
```

---

### Get All Policies

**GET**

```
/api/policies
```

---

### Get Policy By ID

**GET**

```
/api/policies/1
```

---

### Submit Policy

**POST**

```
/api/policies/1/submit
```

---

### Approve Policy

**POST**

```
/api/policies/1/approve
```

---

### Reject Policy

**POST**

```
/api/policies/1/reject
```

---

## Step 10 – Verify Kafka Events

Open Kafka UI in your browser:

```
http://localhost:8080
```

Open the topic:

```
policy-events
```

Verify that the following events have been published:

- POLICY_CREATED
- POLICY_SUBMITTED
- POLICY_APPROVED
- POLICY_REJECTED

---

## Step 11 – Verify Audit Records

Open PostgreSQL and connect to the following database:

```
audit_db
```

Verify that every Kafka event has created a corresponding audit record.

---

# Expected Workflow

```
Client
   │
   ▼
HTTP Request
   │
   ▼
Governance Service
   │
   ▼
Save Policy in governance_db
   │
   ▼
Create PolicyEvent
   │
   ▼
Kafka Producer
   │
   ▼
Kafka Topic (policy-events)
   │
   ▼
Audit Service (Kafka Consumer)
   │
   ▼
Save Audit Record in audit_db
   │
   ▼
Response Returned to Client
```

---

## Summary

The complete execution flow is:

1. The client sends an HTTP request.
2. The Governance Service validates and processes the request.
3. The policy is stored in the `governance_db` database.
4. A `PolicyEvent` is created.
5. The Kafka producer publishes the event to the `policy-events` topic.
6. Kafka stores the event until it is consumed.
7. The Audit Service consumes the event from Kafka.
8. The audit information is stored in the `audit_db` database.
9. The client receives a successful response.

## API Documentation

The Governance Policy Management System uses **Swagger UI (OpenAPI 3)** to provide interactive API documentation.

### Features

- Interactive documentation for all REST endpoints
- View request and response models
- Test APIs directly from the browser
- Automatic documentation generated from Spring Boot controllers
- Displays validation constraints and response codes

### Access Swagger UI

Start the Governance Service and open:

```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI Specification

The generated OpenAPI specification is available at:

```
http://localhost:8080/v3/api-docs
```

This JSON document can be imported into tools such as Postman or other API clients that support the OpenAPI specification.
