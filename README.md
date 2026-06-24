# Urban Bike Infrastructure Management System

A mobile application for a city-wide bike-sharing platform. Users can find and rent bikes, manage payments, and report issues — all from their phone.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Mobile app | Flutter (Android 9+ / iOS 14+) |
| Backend | Java 17 + Spring Boot (microservices) |
| Reverse Proxy | Nginx |
| Message broker | Apache Kafka |
| IoT / MQTT | Eclipse Mosquitto |
| Cache | Redis |
| Databases | PostgreSQL 14, InfluxDB, PostGIS |
| Payments | Stripe |
| Infrastructure | AWS (EC2, Cognito, ALB) |

---

## Features

- **Registration & login** — email + password, email verification required
- **Live bike map** — real-time locations of available bikes and docking stations
- **Bike reservation** — lock a bike for up to 15 minutes before pickup
- **QR code rental** — scan the code on the bike to unlock it automatically
- **Mid-ride pause** — lock the bike temporarily; it stays assigned to you and charges continue
- **Trip completion** — return the bike to a station to end the ride and trigger automatic payment
- **Fault reporting** — report a damaged bike from the app; earn 0.50 PLN if the report is confirmed
- **Wallet** — top up via BLIK, bank transfer, or card; view full transaction history
- **Subscriptions** — daily, weekly, and monthly plans available in-app
