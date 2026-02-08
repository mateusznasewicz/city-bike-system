-- V1__create_tables_and_insert_data.sql

-- 1. Tworzenie tabeli BIKE (dla encji Bike)
CREATE TABLE bike (
    id_roweru VARCHAR(36) NOT NULL PRIMARY KEY,
    kod_qr VARCHAR(255),
    status VARCHAR(50),
    poziom_baterii INTEGER NOT NULL,
    lokalizacja_szerokosc DOUBLE PRECISION NOT NULL,
    lokalizacja_dlugosc DOUBLE PRECISION NOT NULL,
    zasieg_km INTEGER NOT NULL
);

-- 2. Tworzenie tabeli STATION (dla encji Station)
CREATE TABLE station (
    id_stacji VARCHAR(36) NOT NULL PRIMARY KEY,
    nazwa VARCHAR(255),
    lokalizacja_szerokosc DOUBLE PRECISION NOT NULL,
    lokalizacja_dlugosc DOUBLE PRECISION NOT NULL,
    pojemnosc INTEGER NOT NULL,
    dostepne_rowery INTEGER NOT NULL,
    dostepne_stojaki INTEGER NOT NULL
    );

-- 3. Wstawianie danych do tabeli BIKE
-- Uwaga: Rower ID 7 w danych wejściowych nie miał poziomu baterii i zasięgu.
-- Przyjęto wartości domyślne (100% baterii, 0 zasięgu), ponieważ pola w Javie to int (nie mogą być null).

INSERT INTO bike (id_roweru, kod_qr, status, poziom_baterii, lokalizacja_szerokosc, lokalizacja_dlugosc, zasieg_km) VALUES
('1', 'BIKE-00001', 'AVAILABLE', 85, 51.108, 17.039, 25),
('2', 'BIKE-00002', 'AVAILABLE', 92, 51.1095, 17.032, 30),
('3', 'BIKE-00003', 'RENTED', 60, 51.1075, 17.038, 18),
('4', 'BIKE-00004', 'AVAILABLE', 78, 51.105, 17.041, 22),
('5', 'BIKE-00005', 'BROKEN', 15, 51.111, 17.028, 5),
('6', 'BIKE-00006', 'RESERVED', 70, 51.1065, 17.045, 20),
('7', 'BIKE-00007', 'AVAILABLE', 100, 51.102, 17.035, 0),
('8', 'BIKE-00008', 'AVAILABLE', 88, 51.113, 17.04, 28);

-- 4. Wstawianie danych do tabeli STATION
INSERT INTO station (id_stacji, nazwa, lokalizacja_szerokosc, lokalizacja_dlugosc, pojemnosc, dostepne_rowery, dostepne_stojaki) VALUES
 ('2', 'Dworzec Główny', 51.099, 17.035, 30, 18, 12),
 ('3', 'Uniwersytet Wrocławski', 51.114, 17.034, 15, 7, 8),
 ('4', 'Hala Stulecia', 51.1065, 17.077, 25, 20, 5),
 ('5', 'Park Szczytnicki', 51.1085, 17.068, 18, 5, 13),
 ('6', 'Galeria Dominikańska', 51.106, 17.032, 22, 15, 7);