-- ==========================================
-- 1. TWORZENIE TABEL (DDL)
-- ==========================================

CREATE TABLE uzytkownik (
    id_uzytkownika VARCHAR(255) NOT NULL PRIMARY KEY
);

-- Tabela: Rower
CREATE TABLE rower (
   id_roweru VARCHAR(255) NOT NULL PRIMARY KEY,
   status_roweru VARCHAR(255),
   kod_qr VARCHAR(255)
);

-- Tabela: Wypozyczenie
CREATE TABLE wypozyczenie (
  id_wypozyczenia VARCHAR(255) NOT NULL PRIMARY KEY,
  status VARCHAR(255),
  data_rozpoczecia VARCHAR(255),
  data_zakonczenia VARCHAR(255),
  koszt_calkowity DOUBLE PRECISION,
  metoda_uruchomienia VARCHAR(255),
  czas_pauzy_sekundy INTEGER NOT NULL,
  user_id VARCHAR(255),
  bike_id VARCHAR(255),
  CONSTRAINT fk_wypozyczenie_user FOREIGN KEY (user_id) REFERENCES uzytkownik (id_uzytkownika),
  CONSTRAINT fk_wypozyczenie_bike FOREIGN KEY (bike_id) REFERENCES rower (id_roweru)
);

-- Tabela: Fault (Zgloszenie)
CREATE TABLE fault (
   id_zgloszenia VARCHAR(255) NOT NULL PRIMARY KEY,
   typ_usterki VARCHAR(255),
   opis VARCHAR(255),
   data_zgloszenia VARCHAR(255),
   czy_zweryfikowane BOOLEAN NOT NULL,
   czy_potwierdzone BOOLEAN NOT NULL,
   data_weryfikacji VARCHAR(255),
   kwata_nagrody DOUBLE PRECISION,
   user_id VARCHAR(255),
   bike_id VARCHAR(255),
   CONSTRAINT fk_fault_user FOREIGN KEY (user_id) REFERENCES uzytkownik (id_uzytkownika),
   CONSTRAINT fk_fault_bike FOREIGN KEY (bike_id) REFERENCES rower (id_roweru)
);

-- Tabela: Reservation
CREATE TABLE reservation (
     id_rezerwacji VARCHAR(255) NOT NULL PRIMARY KEY,
     status VARCHAR(255),
     data_utworzenia VARCHAR(255),
     data_wygasniecia VARCHAR(255),
     user_id VARCHAR(255),
     bike_id VARCHAR(255),
     CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES uzytkownik (id_uzytkownika),
     CONSTRAINT fk_reservation_bike FOREIGN KEY (bike_id) REFERENCES rower (id_roweru)
);

-- ==========================================
-- 2. INSERTOWANIE DANYCH (DML)
-- ==========================================


INSERT INTO uzytkownik (id_uzytkownika) VALUES ('1');
INSERT INTO uzytkownik (id_uzytkownika) VALUES ('2');


INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('1', 'AVAILABLE', 'BIKE-00001');
INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('2', 'AVAILABLE', 'BIKE-00002');
INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('3', 'RENTED', 'BIKE-00003');
INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('4', 'AVAILABLE', 'BIKE-00004');
INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('5', 'BROKEN', 'BIKE-00005');
INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('6', 'RESERVED', 'BIKE-00006');
INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('7', 'AVAILABLE', 'BIKE-00007');
INSERT INTO rower (id_roweru, status_roweru, kod_qr) VALUES ('8', 'AVAILABLE', 'BIKE-00008');

-- 2.3 Wypozyczenia (mockRentals)
INSERT INTO wypozyczenie (
    id_wypozyczenia,
    user_id,
    bike_id,
    status,
    data_rozpoczecia,
    data_zakonczenia,
    koszt_calkowity,
    metoda_uruchomienia,
    czas_pauzy_sekundy
) VALUES (
             'rental_001',
             '1',
             '3',
             'ACTIVE',
             to_char(NOW() - INTERVAL '10 hours', 'YYYY-MM-DD"T"HH24:MI:SS'),
             NULL,
             0.0,
             'QR',
             0
         );

-- 2.4 Zgloszenia (Faults)
INSERT INTO fault (
    id_zgloszenia,
    bike_id,
    user_id,
    typ_usterki,
    opis,
    data_zgloszenia,
    czy_zweryfikowane,
    czy_potwierdzone,
    data_weryfikacji,
    kwata_nagrody
) VALUES
      (
          'fault_001',
          '1',
          '1',
          'przebita_opona',
          'Tylna opona jest przebita, nie da się jechać',
          '2025-01-15T10:30:00.000Z',
          TRUE,
          TRUE,
          '2025-01-16T14:00:00.000Z',
          5.0
      ),
      (
          'fault_002',
          '3',
          '1',
          'uszkodzone_hamulce',
          'Hamulec przedni nie działa prawidłowo',
          '2025-01-18T15:45:00.000Z',
          TRUE,
          FALSE,
          '2025-01-19T09:00:00.000Z',
          NULL
      ),
      (
          'fault_003',
          '5',
          '1',
          'zepsuty_dzwonek',
          NULL,
          '2025-01-20T08:15:00.000Z',
          FALSE,
          FALSE,
          NULL,
          NULL
      );

-- 2.5 Rezerwacje (mockReservations)
INSERT INTO reservation (
    id_rezerwacji,
    user_id,
    bike_id,
    status,
    data_utworzenia,
    data_wygasniecia
) VALUES (
             'res_001',
             '2',
             '6',
             'ACTIVE',
             '2025-01-28T12:00:00.000Z',
             '2025-01-28T12:15:00.000Z'
         );