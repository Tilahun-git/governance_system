# Governance Policy Management System

A Spring Boot-based Governance Policy Management System that manages policy creation, submission, approval, and rejection workflows.

The system follows a **microservice-oriented and event-driven architecture**, using **Apache Kafka** for asynchronous communication between services. The application and its infrastructure are containerized using **Docker Compose**.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
   - [High-Level Architecture](#high-level-architecture)
   - [Architecture Explanation](#architecture-explanation)
      - [Governance Service](#governance-service)
      - [Apache Kafka](#apache-kafka)
      - [Audit Service](#audit-service)
      - [PostgreSQL](#postgresql)
      - [Kafka UI](#kafka-ui)
- [Technology Stack](#technology-stack)
- [How to Run the Application](#how-to-run-the-application)
   - [Prerequisites](#prerequisites)
   - [1. Clone the Repository](#1-clone-the-repository)
   - [2. Start Docker Desktop](#2-start-docker-desktop)
   - [3. Start the Complete Application](#3-start-the-complete-application)
   - [4. Verify the Containers](#4-verify-the-containers)
   - [5. Access the Services](#5-access-the-services)
   - [6. Stop the Application](#6-stop-the-application)
   - [7. Rebuild After Code Changes](#7-rebuild-after-code-changes)

---

## Project Overview

The Governance Policy Management System is a backend application designed to manage organizational policies throughout their lifecycle.

The system allows users to:

- Create new policies
- View existing policies
- Retrieve policies by ID
- Submit policies for approval
- Approve policies
- Reject policies

The application follows an event-driven architecture where policy-related actions generate events that are published to Apache Kafka and consumed by the Audit Service.

---

## Architecture

The system is designed using a **microservice-oriented architecture** with **asynchronous communication through Apache Kafka**.

### High-Level Architecture

```text
                         Client
                           |
                           | HTTP/REST
                           v
                +-----------------------+
                |   Governance Service  |
                |       Port: 8081      |
                +-----------+-----------+
                            |
                            | Publish Policy Events
                            v
                    +---------------+
                    |  Apache Kafka  |
                    |   Port: 9092   |
                    +-------+-------+
                            |
                            | Consume Policy Events
                            v
                +-----------------------+
                |     Audit Service     |
                |       Port: 8082      |
                +-----------+-----------+
                            |
                            | Store Audit Records
                            v
                    +---------------+
                    |   PostgreSQL   |
                    +---------------+

                    Kafka UI: 8080
```

### Architecture Explanation

#### Governance Service

The **Governance Service** is the main business service responsible for managing policies.

It handles:

- Policy creation
- Policy retrieval
- Policy lifecycle management
- Policy status changes
- Persistence of policy data
- Publishing policy events to Kafka

The Governance Service stores policy data in the `governance_db` PostgreSQL database.

#### Apache Kafka

**Apache Kafka** acts as the asynchronous messaging and event communication layer between the services.

The Governance Service publishes policy events to Kafka, and the Audit Service consumes these events.

This allows the services to communicate asynchronously without requiring direct HTTP communication between them.

#### Audit Service

The **Audit Service** is responsible for consuming policy events from Kafka and recording audit information.

It stores audit records in the `audit_db` PostgreSQL database.

This keeps the audit functionality independent from the main Governance Service.

#### PostgreSQL

**PostgreSQL** is used for persistent data storage.

The application uses separate databases for the two services:

- `governance_db` — stores governance and policy data
- `audit_db` — stores audit data

#### Kafka UI

**Kafka UI** provides a web-based interface for monitoring Apache Kafka and inspecting Kafka topics and messages.

---

## Technology Stack

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Kafka**
- **PostgreSQL**
- **Apache Kafka**
- **Docker**
- **Docker Compose**
- **Maven**
- **Kafka UI**
- **Git**

---

# How to Run the Application

The complete application runs inside Docker containers using Docker Compose.

You **do not need to manually run the Spring Boot services from IntelliJ IDEA** when using the Docker setup.

### Prerequisites

Make sure the following are installed:

- **Docker Desktop**
- **Git**

---

### 1. Clone the Repository

Clone the repository:

```bash
git clone https://github.com/Tilahun-git/governance_system.git
```

Navigate to the project directory:

```bash
cd governance_system
```

Make sure you are in the directory containing the `docker-compose.yml` file.

---

### 2. Start Docker Desktop

Open **Docker Desktop** and make sure the Docker Engine is running.

Verify Docker:

```bash
docker version
```

---

### 3. Start the Complete Application

From the project root directory, run:

```bash
docker compose up -d
```

This starts all required containers:

- Governance Service
- Audit Service
- Apache Kafka
- Kafka UI
- PostgreSQL

Docker Compose also creates the Docker network that allows the containers to communicate with each other.

---

### 4. Verify the Containers

Run:

```bash
docker compose ps
```

You should see containers similar to:

```text
audit-service
governance-service
kafka
kafka-ui
postgres
```

If a container fails to start, check its logs using:

```bash
docker compose logs <service-name>
```

For example:

```bash
docker compose logs governance-service
```

or:

```bash
docker compose logs audit-service
```

---

### 5. Access the Services

The Docker Compose configuration maps the container ports to ports on the host machine.

Therefore, the services can be accessed from your browser or API client using `localhost`.

#### Governance Service

```text
http://localhost:8081
```

#### Audit Service

```text
http://localhost:8082
```

#### Kafka UI

```text
http://localhost:8080
```

> **Note:** `localhost` is used when accessing the services from the host machine.
>
> Inside the Docker network, containers communicate using their Docker service names.

For example:

```text
Governance Service → kafka:9092
Audit Service      → kafka:9092
```

The external ports are exposed to the host as follows:

| Service | Container Port | Host Port |
|---|---:|---:|
| Governance Service | 8081 | 8081 |
| Audit Service | 8082 | 8082 |
| Kafka | 9092 | 9092 |
| Kafka UI | 8080 | 8080 |
| PostgreSQL | 5432 | 5433 |

---

### 6. Stop the Application

To stop the application:

```bash
docker compose down
```

This stops and removes the containers and Docker network created by Docker Compose.

Docker volumes are preserved unless they are explicitly removed.

---
The application will then be available at:

- Governance Service: `http://localhost:8081`
- Audit Service: `http://localhost:8082`
- Kafka UI: `http://localhost:8080`