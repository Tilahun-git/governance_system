# Governance Policy Management System

A Spring Boot-based Governance Policy Management System for managing the lifecycle of governance policies, including creation, submission, approval, and rejection.

The system uses a **microservice-oriented, event-driven architecture**. The Governance Service manages policies and publishes policy events to **Apache Kafka**. The Audit Service consumes these events and stores them as audit records in PostgreSQL.

The complete system is containerized using **Docker Compose**, allowing the entire application and its infrastructure to be started with a single command.

---

## Architecture

The system consists of two Spring Boot microservices and supporting infrastructure.

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
                            | Policy Events
                            v
                    +---------------+
                    | Apache Kafka  |
                    |   Port: 9092  |
                    +-------+-------+
                            |
                            | Consume Events
                            v
                +-----------------------+
                |     Audit Service     |
                |       Port: 8082      |
                +-----------+-----------+
                            |
                            | Audit Records
                            v
                    +---------------+
                    |   PostgreSQL   |
                    +---------------+

                       Kafka UI
                       Port: 8080
```

### Governance Service

The Governance Service is responsible for the policy business operations.

It:

- Creates policies
- Retrieves policies
- Submits policies for approval
- Approves policies
- Rejects policies
- Stores policy information in `governance_db`
- Publishes policy lifecycle events to Kafka

### Apache Kafka

Apache Kafka is used as the event communication layer between the services.

When an important policy action occurs, the Governance Service publishes a `PolicyEvent` to the Kafka topic:

```text
policy-events
```

The Governance Service does not directly call the Audit Service. Instead:

```text
Governance Service
        |
        | publish event
        v
      Kafka
        |
        | consume event
        v
  Audit Service
```

This provides asynchronous communication between the services.

### Audit Service

The Audit Service listens to the `policy-events` Kafka topic.

When it receives a policy event, it processes the event and stores the corresponding audit record in the `audit_db` PostgreSQL database.

Its purpose is to provide traceability of governance actions by maintaining a history of policy-related activities.

### PostgreSQL

PostgreSQL provides persistent storage for both services.

The system uses separate databases:

```text
governance_db
    └── Governance Service data

audit_db
    └── Audit Service data
```

### Kafka UI

Kafka UI provides a web interface for viewing and monitoring Kafka topics, partitions, consumers, and messages.

---

## Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Kafka
- PostgreSQL
- Apache Kafka
- Docker
- Docker Compose
- Maven
- Kafka UI

---

# Running the Application with Docker

The application is configured to run completely inside Docker.

The reviewer does **not** need to manually start Kafka, PostgreSQL, Governance Service, or Audit Service.

Docker Compose starts all required components.

## Prerequisites

Install:

- Docker Desktop
- Git

Make sure Docker Desktop is running before starting the application.

---

## 1. Clone the Repository

Clone the repository:

```bash
git clone https://github.com/Tilahun-git/governance_system.git
```

Enter the project directory:

```bash
cd governance_system
```

Make sure the directory contains:

```text
docker-compose.yml
```

---

## 2. Start the Application

Run:

```bash
docker compose up -d
```

Docker Compose will start:

```text
PostgreSQL
     ↓
Apache Kafka
     ↓
Governance Service
     ↓
Audit Service
     ↓
Kafka UI
```

The Spring Boot services are built from their Dockerfiles and started as containers.

---

## 3. Verify the Containers

Run:

```bash
docker compose ps
```

You should see the following services running:

```text
governance-service
audit-service
kafka
kafka-ui
postgres
```

All containers should show a status similar to:

```text
Up
```

---

## 4. Check Application Logs

If necessary, check the logs of the Governance Service:

```bash
docker compose logs governance-service
```

Check the Audit Service:

```bash
docker compose logs audit-service
```

To follow the logs continuously:

```bash
docker compose logs -f governance-service
```

or:

```bash
docker compose logs -f audit-service
```

---

# Access the Application

After the containers have started successfully:

### Governance Service

```text
http://localhost:8081
```

### Governance Service Swagger UI

```text
http://localhost:8081/swagger-ui/index.html
```

### Audit Service

```text
http://localhost:8082
```

### Audit Service Swagger UI

```text
http://localhost:8082/swagger-ui/index.html
```

### Kafka UI

```text
http://localhost:8080
```

---

# Docker Service Communication

When services communicate **inside Docker**, they use Docker service names instead of `localhost`.

For example:

```text
Governance Service → kafka:9092
Audit Service      → kafka:9092
Governance Service → postgres:5432
Audit Service      → postgres:5432
```

`localhost` refers to the current container itself, so it should not be used for communication between Docker containers.

From the reviewer's browser, however, the exposed ports are accessed through `localhost`:

```text
localhost:8081  → Governance Service
localhost:8082  → Audit Service
localhost:8080  → Kafka UI
localhost:9092  → Kafka
localhost:5432  → PostgreSQL
```

---

# Event Flow

A typical policy operation works as follows:

```text
1. Client
      |
      | HTTP request
      v
2. Governance Service
      |
      | Save/update policy
      v
3. PostgreSQL
      |
      | Policy event
      v
4. Kafka
      |
      | policy-events topic
      v
5. Audit Service
      |
      | Save audit record
      v
6. PostgreSQL
      |
      v
   audit_db
```

For example, when a policy is created:

```text
Client
  ↓
POST /api/policies
  ↓
Governance Service
  ↓
Save Policy
  ↓
Publish POLICY_CREATED event
  ↓
Kafka: policy-events
  ↓
Audit Service consumes event
  ↓
Save audit record
```

The same event-driven mechanism is used for other policy lifecycle actions such as:

```text
POLICY_CREATED
POLICY_SUBMITTED
POLICY_APPROVED
POLICY_REJECTED
```

---

# Rebuild After Code Changes

If source code is changed, rebuild the Docker images:

```bash
docker compose up -d --build
```

This rebuilds the affected Spring Boot services and starts them again.

---

# Stop the Application

To stop and remove the containers:

```bash
docker compose down
```

The PostgreSQL Docker volume is preserved.

To remove the containers **and** the stored PostgreSQL data:

```bash
docker compose down -v
```

> **Warning:** `docker compose down -v` deletes the PostgreSQL volume and therefore removes the persisted database data.

---

# Project Structure

```text
governance_system/
│
├── governance-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── audit-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
│
└── README.md
```

---

## Quick Start

For a reviewer who already has Docker Desktop and Git installed:

```bash
git clone https://github.com/Tilahun-git/governance_system.git

cd governance_system

docker compose up -d --build
```

Then verify:

```bash
docker compose ps
```

Access:

```text
Governance Swagger:
http://localhost:8081/swagger-ui/index.html

Audit Swagger:
http://localhost:8082/swagger-ui/index.html

Kafka UI:
http://localhost:8080
```

To stop:

```bash
docker compose down
```