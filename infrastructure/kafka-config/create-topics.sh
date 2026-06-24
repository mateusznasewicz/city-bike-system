#!/bin/bash

# Temat dla zdarzeń wypożyczeń (np. RentalStarted, RentalEnded)
# Subskrybenci: Payment Service, Analytics Service
kafka-topics --create --if-not-exists --bootstrap-server kafka:29092 --partitions 3 --replication-factor 1 --topic rental.events

# Temat dla zdarzeń floty (np. BikeLocked, ZoneViolation, BatteryLow)
# Subskrybenci: Rental Service (Saga), Analytics Service
kafka-topics --create --if-not-exists --bootstrap-server kafka:29092 --partitions 3 --replication-factor 1 --topic fleet.events

# Temat dla płatności (np. PaymentAuthorized, PaymentDeclined)
# Subskrybenci: Rental Service (Saga)
kafka-topics --create --if-not-exists --bootstrap-server kafka:29092 --partitions 3 --replication-factor 1 --topic payment.events

# Opcjonalnie: Temat telemetryczny (jeśli decydujecie się przepychać surowe dane z MQTT do Kafki)
kafka-topics --create --if-not-exists --bootstrap-server kafka:29092 --partitions 3 --replication-factor 1 --topic iot.telemetry