# City Bike System
## Enterprise-Grade Distributed Microservices Architecture

![Status](https://img.shields.io/badge/status-production--ready-success)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Swarm-2496ED)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## 🎯 Overview

**City Bike System** is a cloud-native, event-driven microservices architecture for managing urban bike-sharing operations at scale. Designed with enterprise-grade reliability, this system orchestrates fleet management, rental operations, and payment processing across distributed infrastructure.

This project demonstrates **production-ready** patterns for:
- **Microservices Architecture** with independent deployments and data isolation
- **Event-Driven Communication** using Apache Kafka for asynchronous workflows
- **Container Orchestration** with Docker Swarm and declarative infrastructure
- **Observability & Monitoring** with InfluxDB time-series data and real-time telemetry
- **CI/CD Pipeline Automation** with Jenkins for zero-downtime deployments
- **MQTT-based IoT Integration** for real-time bike telemetry and control

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     NGINX Load Balancer                      │
│                   (Reverse Proxy & Routing)                  │
└────────────────┬──────────────────────────────────────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
┌───▼────┐  ┌───▼────┐  ┌───▼────┐
│ Fleet  │  │ Rental │  │Payment │
│Service │  │Service │  │Service │
└───┬────┘  └───┬────┘  └───┬────┘
    │            │            │
    │   ┌────────┼────────┐   │
    │   │                 │   │
┌───▼───▼────┐  ┌─────────▼──▼─┐
│  Kafka      │  │  PostgreSQL  │
│ (Event Bus) │  │  (3 Schemas) │
└────────────┘  └──────────────┘

│ Redis Cache │  │ InfluxDB    │
│ (Sessions)  │  │ (Telemetry) │
└────────────┘  └──────────────┘

    │ MQTT Broker │
    │ (IoT Control)
    └──────────────
```

### Core Services

#### **Fleet Service** `[port 8082]`
Manages bike inventory, location tracking, and hardware status.

- **Responsibilities**: Bike registration, status management, geospatial queries, battery monitoring
- **Database**: PostgreSQL with PostGIS extension for location data
- **Key Features**:
  - Real-time GPS coordinate tracking
  - QR code management for bike identification
  - Battery level monitoring
  - MQTT-based hardware communication
  - Redis caching for station data

#### **Rental Service** `[port 8081]`
Handles bike rental bookings, user sessions, and rental lifecycle.

- **Responsibilities**: Rental creation, booking management, session tracking, availability checks
- **Database**: PostgreSQL (isolated schema)
- **Key Features**:
  - User booking and reservation system
  - Real-time bike availability calculation
  - Session management with Redis
  - Event publication (rental events to Kafka)
  - Rate limiting and concurrent access handling


#### **Payment Service** `[port 8083]`
Processes transactions and manages billing for rental charges.

- **Responsibilities**: Payment processing, transaction ledger, billing calculations, refund handling
- **Database**: PostgreSQL (isolated schema)
- **Key Features**:
  - Event-driven payment processing
  - Transaction idempotency for reliability
  - Billing logic and fee calculations
  - Audit trails for compliance
  - Kafka consumer for rental completion events


### Supporting Infrastructure

| Component | Purpose | Details |
|-----------|---------|---------|
| **Apache Kafka** | Event Bus & Stream Processing | Async communication between services, event sourcing, ordering guarantees |
| **PostgreSQL** | Persistent Data Store | 3 isolated databases (fleet, rental, payment), ACID transactions, PostGIS for geospatial |
| **Redis** | Distributed Cache | Session storage, cache-aside pattern, real-time data |
| **InfluxDB** | Time-Series Telemetry | Bike metrics, performance monitoring, metrics aggregation |
| **MQTT Broker** | IoT Command Channel | Real-time hardware control, bike lock/unlock signals |
| **Nginx** | API Gateway | Load balancing, request routing, SSL termination |
| **Docker Swarm** | Container Orchestration | Service replication, rolling updates, network overlay |

---

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Vagrant
- Git

### Local Development

1. **Clone and navigate to project**
   ```bash
   git clone https://github.com/mateusznasewicz/city-bike-system.git
   cd city-bike-system
   ```

2. **Start infrastructure**
   ```bash
   vagrant up
   ```

3. **Build backend services**
   ```bash
   cd backend-java
   ./mvnw clean install
   ```

4. **Run CI/CD pipeline**
   ```bash
   run jenkins pipeline on 192.168.56.200:8080
   ```

---

## 📊 Technology Stack

### Backend
- **Java 17** - Latest LTS with pattern matching and records
- **Spring Boot** - Rapid microservice development framework
- **Spring Data JPA** - ORM with repository pattern
- **Spring Kafka** - Event-driven messaging
- **Spring Integration MQTT** - IoT hardware integration
- **Lombok** - Reduce boilerplate with annotations

### Data & Storage
- **PostgreSQL** - Enterprise-grade relational database with PostGIS
- **Redis (Alpine)** - In-memory data store for caching and sessions
- **InfluxDB** - Time-series database for metrics and telemetry
- **Flyway** - Database migration management

### Messaging & Events
- **Apache Kafka** - Distributed event streaming platform
- **Zookeeper** - Kafka coordination and leader election
- **Eclipse Paho MQTT** - IoT message protocol implementation

### Infrastructure & DevOps
- **Docker** - Container runtime with multi-stage builds
- **Docker Swarm** - Native orchestration with overlay networking
- **Nginx (Alpine)** - High-performance load balancer
- **Jenkins** - CI/CD pipeline orchestration
- **Vagrant** - Infrastructure-as-Code provisioning

### Observability
- **SLF4J with Logback** - Structured logging
- **SpringDoc OpenAPI** - Auto-generated API documentation (Swagger)
- **InfluxDB Integration** - Real-time metrics collection

---

## 🔄 CI/CD Pipeline

The system uses a **multi-stage Jenkins pipeline** for continuous deployment:

```
┌─────────────┐     ┌──────────┐     ┌────────────┐     ┌──────────┐
│   Checkout  │────▶│   Build  │────▶│ Smoke Test │────▶│   Push   │
│    Code     │     │ (Docker) │     │ (Compose)  │     │ Registry │
└─────────────┘     └──────────┘     └────────────┘     └──────────┘
                                                               │
                                                               ▼
                                                        ┌──────────────┐
                                                        │ Deploy Swarm │
                                                        │  (Zero Down) │
                                                        └──────────────┘
```

### Pipeline Stages

1. **Checkout Code** - Clone repository from Git
2. **Build (Docker)** - Compile Java services with Maven, create container images
3. **Smoke Test** - Spin up Docker Compose stack, validate HTTP endpoints
4. **Push to Registry** - Upload images to private Docker registry
5. **Deploy to Swarm** - Rolling deployment via SSH to Docker Swarm manager

---

## 📈 Monitoring & Observability

- **InfluxDB** - Collects time-series data (bike status, rental counts, API latency)
- **Structured Logging** - JSON logs via SLF4J for centralized log analysis
- **API Documentation** - Auto-generated Swagger at `/swagger-ui` per service

---

## 📂 Project Structure

```
city-bike-system/
├── backend-java/                          # All microservices
│   ├── fleet-service/                     # Fleet management
│   │   ├── src/
│   │   │   ├── main/java/pwr/ist/fleetservice/
│   │   │   │   ├── FleetServiceApplication.java
│   │   │   │   ├── controller/           # REST endpoints
│   │   │   │   ├── service/              # Business logic
│   │   │   │   ├── repository/           # Data access
│   │   │   │   ├── entity/               # JPA entities
│   │   │   │   └── event/                # Event handling
│   │   │   ├── resources/
│   │   │   │   ├── application.yml       # Config
│   │   │   │   └── db/migration/         # Flyway scripts
│   │   │   └── test/
│   │   ├── Dockerfile                    # Multi-stage build
│   │   └── pom.xml                       # Maven config
│   │
│   ├── rental-service/                    # Rental management
│   │   └── [Similar structure]
│   │
│   └── payment-service/                   # Payment processing
│       └── [Similar structure]
│
├── docker-compose.yml                     # Local development stack
├── docker-stack.yml                       # Production Swarm stack
├── Jenkinsfile                            # CI/CD pipeline
├── build.sh                               # Build & test script
├── nginx.conf                             # Reverse proxy config
├── kafka-config/                          # Topic initialization
├── mosquitto-config/                      # MQTT broker settings
```

## 📫 Connect & Get in Touch  
💻 **Portfolio:** [mateusznasewicz.dev](https://mateusznasewicz.dev)  
📧 [mateusznasewicz@gmail.com](mailto:mateusznasewicz@gmail.com)  
🔗 [LinkedIn](#)  

---

## License

This project was developed as a university course project. All rights reserved.

