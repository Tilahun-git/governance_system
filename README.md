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

The entire Governance Policy Management System can be run using Docker and Docker Compose.

The reviewer does **not** need to install or configure IntelliJ IDEA, Java, Maven, PostgreSQL, or Kafka separately.

Docker will build and run the Spring Boot services, PostgreSQL, Kafka, and Kafka UI as containers.

---

## Prerequisites

Install:

- [Git](https://git-scm.com/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

Make sure Docker Desktop is running before starting the application.

> **Note:** IntelliJ IDEA is not required to run or test this project. The application is built and started inside Docker containers.
>
> **Optional:** IntelliJ Database, pgAdmin, DBeaver, or another database client can be used only if you want to inspect the PostgreSQL databases and tables directly.

---

# 1. Clone the Repository

Clone the repository:

```bash
git clone https://github.com/Tilahun-git/governance_system.git
```

Enter the project directory:

```bash
cd governance_system
```

Make sure you are inside the project directory that contains:

```text
docker-compose.yml
```

The project structure should look similar to:

```text
governance_system/
├── governance-service/
├── audit-service/
├── postgres/
├── docker-compose.yml
├── .env.example
└── README.md
```

---

# 2. Configure Environment Variables

The project provides an `.env.example` file containing the required environment variables.

Create your own `.env` file from `.env.example`.

### Windows PowerShell

```powershell
Copy-Item .env.example .env
```

### Linux/macOS

```bash
cp .env.example .env
```

You should now have:

```text
.env.example
.env
```

The `.env.example` file is committed to Git so that reviewers know which variables are required.

---

# 3. Start the Complete Application

For the first startup, run:

```bash
docker compose up -d --build
```


> Build the required application images, create the containers, and start the complete application in the background.

---

# 4. Services Started by Docker Compose

Docker Compose starts the following components:

```text
┌───────────────────────────────────────────────┐
│                Docker Desktop                 │
│                                               │
│  ┌──────────────┐                             │
│  │ PostgreSQL   │                             │
│  │    :5432     │                             │
│  └──────────────┘                             │
│                                               │
│  ┌──────────────┐      ┌───────────────┐      │
│  │    Kafka     │◄─────│   Kafka UI    │      │
│  │    :9092     │      │     :8080     │      │
│  └──────┬───────┘      └───────────────┘      │
│         │                                     │
│         │                                     │
│  ┌──────▼──────────┐                          │
│  │    Governance  │                          │
│  │     Service    │                          │
│  │      :8081     │                          │
│  └──────┬─────────┘                          │
│         │                                     │
│         │ PolicyEvent                         │
│         ▼                                     │
│  ┌───────────────┐                            │
│  │ Audit Service │                            │
│  │     :8082     │                            │
│  └───────────────┘                            │
│                                               │
└───────────────────────────────────────────────┘
```

The containers are:

```text
postgres
kafka
kafka-ui
governance-service
audit-service
```

---

# 5. Verify the Containers

After starting the application, run:

```bash
docker compose ps
```

You should see:

```text
governance-service
audit-service
kafka
kafka-ui
postgres
```

The services should normally show a status similar to:

```text
Up
```

If a service is not running, check its logs.

---

# 6. Check Application Logs

### Governance Service

```bash
docker compose logs governance-service
```

### Audit Service

```bash
docker compose logs audit-service
```

### Kafka

```bash
docker compose logs kafka
```

### PostgreSQL

```bash
docker compose logs postgres
```

### Kafka UI

```bash
docker compose logs kafka-ui
```

---

## Follow Logs in Real Time

To continuously watch Governance Service logs:

```bash
docker compose logs -f governance-service
```

To watch Audit Service logs:

```bash
docker compose logs -f audit-service
```

To watch Kafka logs:

```bash
docker compose logs -f kafka
```

Press:

```text
Ctrl + C
```

to stop following the logs.

> `Ctrl + C` only stops displaying the logs. It does not stop the container.

---

# 7. Access the Application

After the containers have started successfully, the services can be accessed from the reviewer's computer using `localhost`.

## Governance Service

The Governance Service runs on:

```text
http://localhost:8081
```


To interact with the Governance API, use Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

---

## Audit Service

The Audit Service runs on:

```text
http://localhost:8082
```


To interact with the Audit API, use Swagger UI:

```text
http://localhost:8082/swagger-ui/index.html
```

---

## Kafka UI

Kafka UI is a separate web application used to visually inspect and manage the Kafka cluster.

Open:

```text
http://localhost:8080
```

Kafka UI can be used to inspect:

- Kafka brokers
- Topics
- Partitions
- Messages
- Consumer groups
- Consumer information

---


### Kafka UI

Kafka UI is a web application that provides a graphical interface for Kafka.

Kafka UI listens for browser requests on:

```text
8080
```

Therefore:

```text
http://localhost:8080
```

opens Kafka UI.


# 9. Docker Service Communication

When services communicate with each other **inside Docker**, they use Docker service names instead of `localhost`.

For example:

```text
Governance Service → kafka:9092
Audit Service      → kafka:9092
Kafka UI           → kafka:9092

Governance Service → postgres:5432
Audit Service      → postgres:5432
```

Docker provides internal DNS, so the service name:

```text
kafka
```

resolves to the Kafka container, and:

```text
postgres
```

resolves to the PostgreSQL container.

---


# 10. PostgreSQL Databases

The PostgreSQL container creates the following databases:

```text
PostgreSQL
│
├── governance_db
│
└── audit_db
```

The databases are created automatically using:

```text
postgres/init.sql
```

The Governance Service uses:

```text
governance_db
```

The Audit Service uses:

```text
audit_db
```

Spring Boot/JPA then creates or updates the required tables.

---

# 11. PostgreSQL Initialization

The file:

```text
postgres/init.sql
```

is executed when PostgreSQL initializes a new data directory.

For example:

```sql
CREATE DATABASE governance_db;
CREATE DATABASE audit_db;
```

Important:

> PostgreSQL initialization scripts run only when the database data directory is initialized for the first time.

If the PostgreSQL Docker volume already exists, changing `init.sql` will not cause it to run again.

If you need to completely recreate the databases, run:

```bash
docker compose down -v
```

Then:

```bash
docker compose up -d --build
```

> **Warning:** `docker compose down -v` deletes the PostgreSQL Docker volume and therefore deletes the stored database data.

---

# 13. Test the Governance → Kafka → Audit Flow

## Step 1 — Open Governance Swagger

Open:

```text
http://localhost:8081/swagger-ui/index.html
```

Use the available policy endpoints to create or modify a policy.

For example:

```text
POST /api/policies
```

When the policy is successfully created, the Governance Service publishes a:

```text
POLICY_CREATED
```

event to:

```text
policy-events
```

---

## Step 2 — Check Kafka UI

Open:

```text
http://localhost:8080
```

Go to:

```text
Topics
    ↓
policy-events
```

You should be able to see the published Kafka messages.

The event may contain information such as:

```json
{
  "policyId": 1,
  "eventType": "POLICY_CREATED",
  "actor": "Tilahun",
  "timestamp": "2026-09-02T..."
}
```

---

## Step 3 — Audit Service Consumes the Event

The Audit Service listens to:

```text
policy-events
```

When Kafka delivers the event:

```text
Kafka
   |
   | PolicyEvent
   v
Audit Service
```

the Audit Service saves an audit record into:

```text
audit_db
```

---

## Step 4 — Check the Audit Service

Open:

```text
http://localhost:8082/swagger-ui/index.html
```

Use the audit endpoint to retrieve the audit history.

For example:

```text
GET /api/audit/policies/{id}
```

You should see the corresponding audit record.

This confirms that the complete event-driven flow is working:

```text
Governance
    ↓
Kafka
    ↓
Audit
    ↓
audit_db
```

---

# 15. Start an Individual Service

You do not always need to start the entire application.

To start only the Governance Service:

```bash
docker compose up -d governance-service
```

To start only the Audit Service:

```bash
docker compose up -d audit-service
```

To start Kafka:

```bash
docker compose up -d kafka
```

To start Kafka UI:

```bash
docker compose up -d kafka-ui
```

To start PostgreSQL:

```bash
docker compose up -d postgres
```

If a service has dependencies defined using `depends_on`, Docker Compose may also start those dependencies.

For example:

```bash
docker compose up -d governance-service
```

may also start:

```text
postgres
kafka
governance-service
```

---

# 16. Stop an Individual Service

To stop only the Governance Service:

```bash
docker compose stop governance-service
```

To stop only the Audit Service:

```bash
docker compose stop audit-service
```

To stop Kafka:

```bash
docker compose stop kafka
```

To stop Kafka UI:

```bash
docker compose stop kafka-ui
```

To stop PostgreSQL:

```bash
docker compose stop postgres
```

The containers are stopped but not removed.

---

# 17. Start a Previously Stopped Service

If a service was stopped using:

```bash
docker compose stop audit-service
```

start it again using:

```bash
docker compose start audit-service
```

For Governance Service:

```bash
docker compose start governance-service
```

For Kafka:

```bash
docker compose start kafka
```

For PostgreSQL:

```bash
docker compose start postgres
```

---

# 18. Restart an Individual Service

To restart Governance Service:

```bash
docker compose restart governance-service
```

To restart Audit Service:

```bash
docker compose restart audit-service
```

To restart Kafka:

```bash
docker compose restart kafka
```

To restart PostgreSQL:

```bash
docker compose restart postgres
```

To restart Kafka UI:

```bash
docker compose restart kafka-ui
```

A restart does not rebuild the Docker image.

---

# 19. Rebuild After Code Changes

If you change Java source code or another file that is copied into a Docker image, rebuild the affected service.

For example, after changing Governance Service:

```bash
docker compose up -d --build governance-service
```

After changing Audit Service:

```bash
docker compose up -d --build audit-service
```

If both services were changed:

```bash
docker compose up -d --build governance-service audit-service
```

To rebuild everything:

```bash
docker compose up -d --build
```

---

# 20. Rebuild Without Docker Cache

Normally Docker reuses cached build layers.

If you need to perform a completely fresh image build:

```bash
docker compose build --no-cache governance-service
```

Then start the service:

```bash
docker compose up -d governance-service
```

For the Audit Service:

```bash
docker compose build --no-cache audit-service
```

Then:

```bash
docker compose up -d audit-service
```

> `--no-cache` is normally not required. Use it when you suspect Docker's build cache is causing a problem.

---

# 21. Stop the Entire Application

To stop all project containers:

```bash
docker compose stop
```

This stops the containers but does not remove them.

You can start them again using:

```bash
docker compose start
```

---

# 22. Stop and Remove the Containers

To stop and remove all project containers:

```bash
docker compose down
```

This removes:

- Application containers
- Kafka container
- Kafka UI container
- PostgreSQL container
- Docker Compose network

The PostgreSQL named volume is preserved, so database data remains available.

To start the application again:

```bash
docker compose up -d
```

---

# 23. Completely Reset the Application

If you want to remove the containers **and** the PostgreSQL Docker volume:

```bash
docker compose down -v
```

Then start everything again:

```bash
docker compose up -d --build
```

This creates a fresh PostgreSQL data directory and runs:

```text
postgres/init.sql
```

again.

> **WARNING:** `docker compose down -v` permanently removes the PostgreSQL Docker volume and therefore deletes the stored database data.

Use this command only when you intentionally want a fresh database.

---

# 24. Useful Docker Commands

### Show project containers

```bash
docker compose ps
```

### Show all Docker containers

```bash
docker ps -a
```

### Show Docker images

```bash
docker images
```

### Show Docker volumes

```bash
docker volume ls
```

### Show all project logs

```bash
docker compose logs -f
```

