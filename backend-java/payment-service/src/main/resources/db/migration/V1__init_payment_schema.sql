-- ==========================================
-- 1. TWORZENIE TABEL (DDL)
-- ==========================================

-- Tabela: Uzytkownik (Płatności)
CREATE TABLE uzytkownik (
    id VARCHAR(255) NOT NULL PRIMARY KEY
);

-- Tabela: Wallet
CREATE TABLE wallet (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    user_id VARCHAR(255) UNIQUE,
    saldo DOUBLE PRECISION NOT NULL,
    zablokowane_srodki DOUBLE PRECISION NOT NULL,
    status VARCHAR(255),
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES uzytkownik (id)
);

-- Tabela: PaymentMethod
CREATE TABLE payment_method (
    id_metody VARCHAR(255) NOT NULL PRIMARY KEY,
    typ VARCHAR(255),
    ostatnie_cztery VARCHAR(255),
    marka_karty VARCHAR(255),
    wallet_id VARCHAR(255),
    CONSTRAINT fk_payment_method_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (id)
);

-- Tabela: Platnosc
CREATE TABLE platnosc (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  wallet_id VARCHAR(255),
  typ_platnosci VARCHAR(255),
  kwota DOUBLE PRECISION NOT NULL,
  czas_zlecenia VARCHAR(255),
  opis VARCHAR(255),
  CONSTRAINT fk_platnosc_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (id)
);


-- 2.1 Uzytkownicy
INSERT INTO uzytkownik (id) VALUES ('1');
INSERT INTO uzytkownik (id) VALUES ('2');

-- 2.2 Portfele (Wallets)
INSERT INTO wallet (id, user_id, saldo, zablokowane_srodki, status) VALUES
('1', '1', 150.50, 0.0, 'ACTIVE'),
('2', '2', -25.00, 0.0, 'DEBT');

-- 2.3 Metody Płatności (PaymentMethods)
-- Uwaga: W encji PaymentMethod pole id to id, a w mocku id_metody.
INSERT INTO payment_method (id_metody, typ, ostatnie_cztery, marka_karty, wallet_id) VALUES
('pm_001', 'CARD', '4242', 'Visa', '1'),
('pm_002', 'BLIK', NULL, NULL, '1');

-- 2.4 Płatności (Transactions -> Platnosc)
-- Mapowanie mocka transactions do pól encji Platnosc
INSERT INTO platnosc (id, wallet_id, typ_platnosci, kwota, czas_zlecenia, opis) VALUES
('trans_001', '1', 'TOP_UP', 100.00, '2024-01-15T10:30:00.000', 'Doładowanie przez BLIK'),
('trans_002', '1', 'FEE', -5.50, '2024-01-16T14:20:00.000', 'Opłata za wynajem roweru'),
('trans_003', '1', 'TOP_UP', 50.00, '2024-01-17T09:00:00.000', 'Doładowanie kartą'),
('trans_004', '1', 'REWARD', 10.00, '2024-01-18T12:00:00.000', 'Bonus za polecenie'),
('trans_005', '1', 'FEE', -4.00, '2024-01-19T16:45:00.000', 'Opłata za wynajem roweru');